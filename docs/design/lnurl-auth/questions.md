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

public Optional<LinkingKey> findPairedLinkingKeyByK1(K1 k1) {
// This method returns the linking key associated with the k1 passed as parameter.
// TODO What is it used for ? who calls this method ?
it seems you say ity's useless to store it so why do you store it ???