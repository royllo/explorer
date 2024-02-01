# Bitcoin & Java: a guide to LNURL-auth with Spring Boot.

[LNURL-auth](https://lightninglogin.live/) is an authentication protocol that authenticates a user using only a digital
signature. The protocol can be used as a replacement for email/username, password, social media, 2FA, forgotten
password... A big win for security and privacy. All you need to register/login is a lightning wallet that supports
LNURL.

This is the solution I choose to manage users in my
project [Royllo (a search engine/explorer for Taproot Assets)](https://explorer.royllo.org/). To implement it, I found a
wonderful spring boot
starter: [https://github.com/theborakompanioni/bitcoin-spring-boot-starter](https://github.com/theborakompanioni/bitcoin-spring-boot-starter).

-----

## How it works for the user?

The first step is, of course, to have a compatible lightning wallet like [phoenix](https://phoenix.acinq.
co/) (by the way, you don't need to own bitcoins).

When you go to a website using LNURL-auth like [lightninglogin.live](https://lightninglogin.live/), you click on the
login button, and you will see a qr code appears:

![lightninglogin.live login](lightninglogin.login.png)

Start your lighting wallet, click on send button, scan the qr code, and you are authenticated, voilà! no email,
password, phone number… have been exchanged.

-----

## How it works on the technical side?

_The specification can be found here: [LUD-04: auth base spec](https://github.com/lnurl/luds/blob/luds/04.md)._

### Step 1: The server generates a login url (encoded in the QR code).

Each time a new login request arrives to the server, a new lnurl-auth url is generated, and it looks like this:
`https://site.com?tag=login&k1=RANDOM`.

The `tag` parameter is used to specify the type of action or operation that the LNURL is intended for. In our case, it
is set to `login` to indicate that the LNURL is intended for authentication.

The `k1` parameter is a randomly generated token that serves as a challenge. It is unique for each authentication
request. As we will see later, the lightning wallet is expected to sign this `k1` token.

### Step 2: The user scans the QR code with its lighting wallet.

After scanning the QR code with your lighting wallet, your application will decode the two parameters (`tag` & `k1`) and
retrieve the domain name used (in our example `site.com`).

The domain name is then used by the wallets as material for key derivation. This means the master key of your wallet
plus the domain will generate what we call a `linking key` that will only be used for `site.com`. It's called
the `linking private key`.

### Step 3: The lighting wallet calls the server login page.

Your lightning wallet will now call the login page with the `sig` and `key` parameters like this:

```
sig=<hex(sign(utf8ToBytes(k1), linkingPrivKey))>&key=<hex(linkingKey)>
```

The `sig` parameter contains the signature of the `k1` value using the `linking private key`.
The `key` parameter contains the `linking public key`.

### Step 4: The server makes the verification.

The server receives the `sig` parameter that should contain the signature of the `k1` value made by the wallet with the
`linking private key`.

Now, the server will verify the `k1` value's signature created by the lighting wallet and transmitted with the `sig`
parameter. This is done quite simply by using the `linking public key` transmitted with the `key` parameter.

After this verification, the user can be authenticated and/or created.

-----

## Using bitcoin-spring-boot-starter.

Integrating LNURL-auth in Java/Spring boot is quite simple
using https://github.com/theborakompanioni/bitcoin-spring-boot-starter.

First constraint: in order to use LNURL-auth, you must serve your application over https (no self-signed cert allowed).
To make it easy, I advise you to use [ngrok](https://ngrok.com). It will create a public address with a valid SSL
that will serve your local web server.

It's quite simple, create a ngrok account, install the ngrok application and configure the token. Then, just
run: `ngrok http 8080` and you will get:

```
Session Status online
...
Forwarding https://6a8b-92.ngrok-free.app -> http://localhost:8080
```

When you will type `https://6a8b-92.ngrok-free.app` in your browser, you will access your local server with a valid SSL
certificate.

### Adding bitcoin-spring-boot-starter.

Adding bitcoin-spring-boot-starter is quite easy, add this dependency to your java spring boot project:

```xml

<dependencies>
    ...
    <dependency>
        <groupId>io.github.theborakompanioni</groupId>
        <artifactId>spring-lnurl-auth-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>io.github.theborakompanioni</groupId>
        <artifactId>lnurl-simple</artifactId>
    </dependency>
    ...
</dependencies>
```

### Database & domain objects modification.

To represent a user, we already have
a [User](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/domain/user/User.java)
domain object and
a [user database script](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/resources/db/changelog/1.0.0/table/table-application_user.xml)
to create the corresponding table. Nothing special here.

We now add
a [UserLnurlAuthKey](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/domain/user/UserLnurlAuthKey.java)
object,
its [repository](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/repository/user/UserLnurlAuthKeyRepository.java),
and
its [sql creation script](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/resources/db/changelog/1.0.0/table/table-application_user_lnurl_auth_linking_key.xml).

This object is used to store all the linking keys and corresponding k1 of users.

### Adding a k1 manager.

Your application will generate k1 values for each login request. Maybe a wallet will call your url with a `k1` value
or maybe not but for sure, if your server receives an authentication attempt from a wallet, it will need to verify
that the k1 was generated.

To keep it simple, you can use a simple `k1` manager like the one provided by the spring boot
starter: [SimpleK1Manager](https://github.com/theborakompanioni/bitcoin-spring-boot-starter/blob/master/incubator/spring-lnurl/spring-lnurl-auth-simple/src/main/java/org/tbk/lnurl/auth/SimpleK1Manager.java)

In my case, I have several front servers and I need to share the `k1` values between them. So I build a `k1` store to
store them in my database so any server could view them. It's quite simple:

1 - I have a very simple table named `UTIL_K1_CACHE` to store `k1`
values crated by
this [database script](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/resources/db/changelog/1.0.0/table/table-util_k1_cache.xml).

2 -
A [K1Value](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/domain/util/K1Value.java)
object to represent a `k1` value in database.

3 - A
simple [K1ValueRepository](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/repository/util/K1ValueRepository.java)
to manage the `k1` values in database.

4 -
A [DatabaseK1Manager](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/util/DatabaseK1Manager.java)
to create, validate and invalidate `k1` stored in database.

### Implements LnurlAuthPairingService.

`LnurlAuthPairingService` is an interface provided by `bitcoin-spring-boot-starter` that you must implement. It declares
two methods:

**pairK1WithLinkingKey**: This method is called by the spring boot starter when a user has provided a `k1` signed
with a private linking key and its public linking key.

Usually, this is where you search if the linking key already exists in the database. If not, you create a new user and
store its linking key. If the user exist, you just return the user, and you update the `k1` he used.

_Note that when you create the user, we don't know anything about him except its linking key. So we use its key as
its username._

**findPairedLinkingKeyByK1**: This method returns the linking key associated with the `k1` passed as parameter.

We decided to implement the two methods directly it in our
existing [UserService](https://github.com/royllo/explorer/blob/ee6cc7c7fad49c6205a9b4422f23bf4c36c0432e/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java#L121).

### Implements UserDetailsService.

`UserDetailsService` is used throughout the Spring framework as a user DAO. The method `UserDetails loadUserByUsername(
String username)` must be implemented to return a `UserDetails` object that matches the provided username.

Again, We decided to implement direct it in our
existing [UserService](https://github.com/royllo/explorer/blob/ee6cc7c7fad49c6205a9b4422f23bf4c36c0432e/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/service/user/UserServiceImplementation.java#L160).

The implementation is quite simple, the `username` parameter will be filled by the framework with the linking key,
so we just search the corresponding user by its linking key thanks to
our [UserLnurlAuthKey](https://github.com/royllo/explorer/blob/development/backend/explorer-core/autoconfigure/src/main/java/org/royllo/explorer/core/domain/user/UserLnurlAuthKey.java)
object.

### lnurlAuthFactory & k1Manager

We now create
a [LnurlAuthConfiguration](https://github.com/royllo/explorer/blob/development/backend/servers/explorer-web/src/main/java/org/royllo/explorer/web/configuration/LnurlAuthConfiguration.java#L54)
class that where we will declare two beans:

[K1Manager](https://github.com/royllo/explorer/blob/ee6cc7c7fad49c6205a9b4422f23bf4c36c0432e/backend/servers/explorer-web/src/main/java/org/royllo/explorer/web/configuration/LnurlAuthConfiguration.java#L41)
This is where we instantiate our `k1` manager as a bean.

[LnurlAuthFactory](https://github.com/royllo/explorer/blob/ee6cc7c7fad49c6205a9b4422f23bf4c36c0432e/backend/servers/explorer-web/src/main/java/org/royllo/explorer/web/configuration/LnurlAuthConfiguration.java#L54C5-L54C21)
`LnurlAuthFactory` is the class that will be used to generate the lnurl-auth URL and the `k1`that will be used the user
wallet.

### SecurityConfiguration

To secure our application, we use spring security and, to configure it, we start by declaring
a [SecurityConfiguration](https://github.com/royllo/explorer/blob/development/backend/servers/explorer-web/src/main/java/org/royllo/explorer/web/configuration/SecurityConfiguration.java)
class with the `@EnableWebSecurity` annotation.

The method to implement
is [public SecurityFilterChain filterChain(final HttpSecurity http)](https://github.com/royllo/explorer/blob/ee6cc7c7fad49c6205a9b4422f23bf4c36c0432e/backend/servers/explorer-web/src/main/java/org/royllo/explorer/web/configuration/SecurityConfiguration.java#L46).

Two things are important there:

- The `.authorizeHttpRequests` method that declares the url that should be secured. In our case, we want to secure all
  the urls behind `:account` and allow anything else;
- The `.with(new LnurlAuthConfigurer(), lnurlAuthConfigurer -> ...)` method that declares the lnurl-auth
  configuration. First, we declare all the services we build, we set the URL configuration, login url,
  the session endpoint and the wallet endpoint.

## Conclusion

LNURL-auth is a great protocol to authenticate users without exchanging any personal information. It is quite easy
to implement in Java/Spring boot with the bitcoin-spring-boot-starter and @theborakompanioni provides an amazing
support !

So let's go and let's build a future where we don't need to give our email, phone number, password, social media... !