# Common test

This project contains the data (assets & transactions) used by tests in all projects.
It also allows to create mocker servers for mempool and tapd with those data.

## Process

If you run `get_and_run_tapd`, it will download tade and run it locally.
If you run `retrieve_macaroon_from_tapd`, it will retrieve the macaroon from the tapd container.

In the `justfile`, you have other commands to retrieve the data from our local tapd and the public mempool server.

## Data

### roylloCoin

This coin is already saved in Royllo database, this is the only one you can use when you start Royllo.
**fixed supply** : you won't find an asset group.

### roylloNFT

This asset is used to test NFT.
**fixed supply** : you won't find an asset group.

### setOfRoylloNFT

this asset is a collection of NFT.
**Emission possible** : you will have a group anchor.

### trickyRoylloCoin

This coin is used to test the case of a coin with several states.
**fixed supply** : you won't find an asset group.

### unknownRoylloCoin

This coin is used to test the case when Royllo doesn't know the coin.
**fixed supply** : you won't find an asset group.

### unlimitedRoylloCoin

This coin is used to test the case of a coin with an asset group and two emissions.
**Emission possible** : you will have an asset group and two emissions.
