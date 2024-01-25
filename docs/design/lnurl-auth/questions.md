# Lnurl-Auth

I don't see why this method is needed ?

```java

@Override
public Optional<LinkingKey> findPairedLinkingKeyByK1(K1 k1) {
// This method returns the linking key associated with the k1 passed as parameter.
// TODO Ask what is it used for ?
    return Optional.empty();
}
```

linkingKey.toHex() is used to create the username right ? I guess we don't have the choice at first.

How come you say it's the least K1 and you have this code :

```java
WalletUser pair(LinkingKey linkingKey, K1 k1) {
    this.lastSuccessfulAuthAt = Instant.now().toEpochMilli();

    linkingKeys.stream()
            .filter(it -> it.getLinkingKey().equals(linkingKey.toHex()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Given 'linkingKey' not found."))
            .markUsedK1(k1);

    registerEvent(new WalletUserLoginSuccessfulEvent(this.id));

    return this;
}
```

public Optional<LinkingKey> findPairedLinkingKeyByK1(K1 k1) {
// This method returns the linking key associated with the k1 passed as parameter.
// TODO What is it used for ? who calls this method ?