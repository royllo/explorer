# Lnurl-Auth

In the lnurl_auth_linking_key table you create here:

- Why do you store the least_recently_used_k1 ? when is this data queried ?
- Why is it the least recently used k1 instead of the most recently used k1 ?

- https://github.com/theborakompanioni/bitcoin-spring-boot-starter/blob/master/incubator/spring-lnurl/spring-lnurl-auth-example-application/src/main/java/org/tbk/lightning/lnurl/example/db/migration/V1__init.java

Why, in your example, do you separate LnurlAuthPairingService and WalletUserService ?
What do you think would be the problem to have WalletUserService implement LnurlAuthPairingService ?