# Universe - Design documentation

## Introduction

Universe is a service and a service may know about multiple assets (Multiverse) or only about a single output (Pocket
Universe).

An Asset Universe is a publicly auditable merkle-sum sparse merkle tree (MS-SMT) over one or several assets. Unlike the
normal Taproot Asset tree, a Universe isn’t used to custody Taproot Assets. Instead, a Universe commits to a subset of
the history of one, or many assets. A close analogue to a public Universe is a block explorer

The announcement is [here](https://lightning.engineering/posts/2023-05-16-taproot-assets-v0.2/) and The BIP is [here]
(https://github.com/Roasbeef/bips/blob/da3049f2f65b1c1c2b2caa7164449192b791bd29/bip-tap-universe.mediawiki). There is
also a video explaining the 0.2 [here](https://www.youtube.com/watch?v=8Qi7VOvKe5o&feature=youtu.be).

## API & test server

We can use the lighting labs test server:
https://testnet.universe.lightning.finance/v1/taproot-assets/universe/leaves/asset-id/02a4d3ce40a0d96deb7dcbd1eb565b8c46e4f5366260cecf848364b2a7fb2c5a

The following methods are available without macaroon on a universe server:

- [AssetRoots](https://lightning.engineering/api-docs/api/taproot-assets/universe/asset-roots)
- [QueryAssetRoots](https://lightning.engineering/api-docs/api/taproot-assets/universe/query-asset-roots)
- [AssetLeafKeys](https://lightning.engineering/api-docs/api/taproot-assets/universe/asset-leaf-keys)
- [AssetLeaves](https://lightning.engineering/api-docs/api/taproot-assets/universe/asset-leaves)
- [QueryProof](https://lightning.engineering/api-docs/api/taproot-assets/universe/query-proof)
- [InsertProof](https://lightning.engineering/api-docs/api/taproot-assets/universe/insert-proof)

The complete API is [here](https://lightning.engineering/api-docs/category/universe-service/index.html).

You can see the database script of the taproot assets
daemon [here](https://github.com/lightninglabs/taproot-assets/blob/main/tapdb/sqlc/migrations/000007_universe.up.sql).

## What to do ?

Allow people to add their universe server (as a request) to royllo (throw an add universe request).

Create a batch that retrieve assets and proofs from others universe servers and add them to our database so people
could search for them and their proofs. Royllo can display richer information, and make the data more accessible

Then :

- Implement all universe public methods so people could add royllo as a federation server. their daemon will
  automatically sync stuff, and also push new things (right now just new assets, in the future also transactions) to
  it. Instead of getting data from distant servers, the servers will push data to us.
  s

## What are the steps ?

1 - Call universe/roots to get the list of assets the universe knows about:

```bash
curl https://testnet.universe.lightning.finance/v1/taproot-assets/universe/roots | jq
```

You can search for one with:

```bash
curl https://testnet.universe.lightning.finance/v1/taproot-assets/universe/roots/asset-id/f84238ffd7838b663f1800d8147c9338f15688b430f6e9d8d53f148049ef3bcb | jq`
```

note : A UniverseKey is composed of the Universe ID (asset_id/group_key) and also a leaf key (outpoint || script_key)

2 - Call asset leaves (the values in the Universe MS-SMT tree) for a each asset_id retrieved (or group_key). Those
represents either asset issuance events (they have a genesis witness) or asset transfers that took place on chain. The
leaves contain a normal Taproot Asset proof, as well as details for the asset.

```bash
curl https://testnet.universe.lightning.finance/v1/taproot-assets/universe/leaves/asset-id/f84238ffd7838b663f1800d8147c9338f15688b430f6e9d8d53f148049ef3bcb |jq
```

3 - Decode the proof retrieved in the previous step and it's ok, add the proof to our database throw an add proof
request.

## Backup

curl https://testnet.universe.lightning.finance/v1/taproot-assets/universe/keys/asset-id/f84238ffd7838b663f1800d8147c9338f15688b430f6e9d8d53f148049ef3bcb |
jq


