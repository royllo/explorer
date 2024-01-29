# Lnurl-Auth

Login successful but user not authenticated

I think I'm close to have something working! and the blog post is close to be done too.

But I'm stuck at this step: login page is displayed, I scan the QR code, my wallet says "authentication success",
but I'm stuck on the login page, no redirect and if I manually go back to home page where there is this thymeleaf code:

```html
<p sec:authentication="name"></p>
```

It says anonymous user

When I check the logs, I can the user is crated and the SecurityContextHolder is set:

```
s.s.LnurlAuthSessionAuthenticationFilter : got lnurl-auth session migration request for k1 '0f0dbde709f33c8879093987ba53e22eb5eaa2aa0f7acdfd227f598bd01957e5'
o.r.e.c.s.u.UserServiceImplementation    : Finding the paired linking key for k1 0f0dbde709f33c8879093987ba53e22eb5eaa2aa0f7acdfd227f598bd01957e5
o.r.e.c.s.u.UserServiceImplementation    : Linking key not found

.s.w.LnurlAuthWalletAuthenticationFilter : got lnurl-auth wallet authentication request for k1 '55c5460f801a6241d42ac21f86f15d4bf7d176e4acd660e58bff988632e51719'
o.r.e.c.s.u.UserServiceImplementation    : Creating the user for the linking key: 02ed9953f5e3a7a79b943ffa333f08bdfe42373199b21b91a77511d384344bd1c2
o.r.e.c.s.u.UserServiceImplementation    : Creating a user with username: 02ed9953f5e3a7a79b943ffa333f08bdfe42373199b21b91a77511d384344bd1c2
o.r.e.c.s.u.UserServiceImplementation    : User created: User(id=3, userId=b486f731-1335-4d0c-ad5c-1ca509a166aa, username=02ed9953f5e3a7a79b943ffa333f08bdfe42373199b21b91a77511d384344bd1c2, role=USER)
.s.w.LnurlAuthWalletAuthenticationFilter : Set SecurityContextHolder to LnurlAuthWalletToken [Principal=02ed9953f5e3a7a79b943ffa333f08bdfe42373199b21b91a77511d384344bd1c2, Credentials=[PROTECTED], Authenticated=true, Details=org.springframework.security.core.userdetails.User [Username=02ed9953f5e3a7a79b943ffa333f08bdfe42373199b21b91a77511d384344bd1c2, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[USER]], Granted Authorities=[USER]]
rlAuthWalletAuthenticationSuccessHandler : Received successful lnurl-auth wallet request of user '02ed9953f5e3a7a79b943ffa333f08bdfe42373199b21b91a77511d384344bd1c2'

s.s.LnurlAuthSessionAuthenticationFilter : got lnurl-auth session migration request for k1 '94b1f3fa2cfcea4ece82b193375f0d5e51b7c83c0d2817657da2f479f7a5e3b5'
o.r.e.c.s.u.UserServiceImplementation    : Finding the paired linking key for k1 94b1f3fa2cfcea4ece82b193375f0d5e51b7c83c0d2817657da2f479f7a5e3b5
o.r.e.c.s.u.UserServiceImplementation    : Linking key not found

s.s.LnurlAuthSessionAuthenticationFilter : got lnurl-auth session migration request for k1 '93a030bff5a07fd26930b1ec2d8535e3a6f15531627969e967b2fb25406657ed'
o.r.e.c.s.u.UserServiceImplementation    : Finding the paired linking key for k1 93a030bff5a07fd26930b1ec2d8535e3a6f15531627969e967b2fb25406657ed
o.r.e.c.s.u.UserServiceImplementation    : Linking key not found
```

I'm using `0.12.0` and this is what I did:

## DatabaseK1Manager

I
created
a [DatabaseK1Manager](https://github.com/royllo/explorer/blob/461-user-asset-data-management/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/util/DatabaseK1Manager.java)
that implements your K1Manager but stores the k1 in database.

## UserLnurlAuthKey

I added a domain object
named [UserLnurlAuthKey](https://github.com/royllo/explorer/blob/461-user-asset-data-management/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/domain/user/UserLnurlAuthKey.java)
linked to the user. It has a `linkingKey` and a `k1` fields.

## Implements LnurlAuthPairingService

I re-used my user service to
implement [LnurlAuthPairingService](https://github.com/royllo/explorer/blob/461-user-asset-data-management/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java)

[pairK1WithLinkingKey](https://github.com/royllo/explorer/blob/0b4446ef1335f26d4bbc5d0e75460d1e998f51af/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java#L121C20-L121C40)
If the linking key is not already in our database, I create a new user, and it's UserLnurlAuthKey object else I only
update the k1.

[findPairedLinkingKeyByK1](https://github.com/royllo/explorer/blob/0b4446ef1335f26d4bbc5d0e75460d1e998f51af/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java#L146C33-L146C57)
Looking at UserLnurlAuthKey, I search by k1 and returns the linking key if found.

## Implements UserDetailsService

I re-used my user service to
implement [UserDetailsService](https://github.com/royllo/explorer/blob/461-user-asset-data-management/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java#L146C33-L146C57)

[loadUserByUsername](https://github.com/royllo/explorer/blob/0b4446ef1335f26d4bbc5d0e75460d1e998f51af/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java#L160C24-L160C42)
I search a user by its linking key (I saw you search by username in your example but i may let the user change its
username).

## lnurlAuthFactory

i copied on what you and change the url everytime to nrgok https url:
[lnurlAuthFactory](https://github.com/royllo/explorer/blob/461-user-asset-data-management/backend/servers/explorer
-web/src/main/java/org/royllo/explorer/web/configuration/OtherLnurlAuthConfiguration.java)

## securityConfiguration

I did, I think, the same thing as
you: [SecurityConfiguration](https://github.com/royllo/explorer/blob/461-user-asset-data-management/backend/servers/explorer-web/src/main/java/org/royllo/explorer/web/configuration/SecurityConfiguration.java)