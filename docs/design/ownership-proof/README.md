# Proving you own an asset

Using [ProveAssetOwnership](https://lightning.engineering/api-docs/api/taproot-assets/asset-wallet/prove-asset-ownership)
_ProveAssetOwnership creates an ownership proof embedded in an asset transition proof. That ownership proof is a signed
virtual transaction spending the asset with a valid witness to prove the prover owns the keys that can spend the asset._

## Creating an asset

```bash
docker exec -it 98a71d32306d tapcli assets mint \
                --type collectible \
                --name ownershipTest1 \
                --supply 1 \
                --meta_bytes "ownershipTest1"
```

We emit the transaction.

```bash
docker exec -it 98a71d32306d \
                tapcli assets mint finalize
```

Creation result:

```json
{
  "batch": {
    "batch_key": "026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6",
    "batch_txid": "d19ef276802f039570682cd15afcb8d1455632fdf818990fa28e0acf503b1ab1",
    "state": "BATCH_STATE_BROADCAST",
    "assets": [
      {
        "asset_version": "ASSET_VERSION_V0",
        "asset_type": "COLLECTIBLE",
        "name": "ownershipTest1",
        "asset_meta": {
          "data": "6f776e6572736869705465737431",
          "type": "META_TYPE_OPAQUE",
          "meta_hash": "c695c4d63e2ce368b87bd4ca6610bf3bc48a701f6942a8eb5647b0405e518eaf"
        },
        "amount": "1",
        "new_grouped_asset": false,
        "group_key": "",
        "group_anchor": ""
      }
    ]
  }
}
```

`docker exec -it 98a71d32306d tapcli assets list` returns:

```json
{
  "version": "ASSET_VERSION_V0",
  "asset_genesis": {
    "genesis_point": "31f4361174c4abf891a0b10bb2c1062cad074649b107c4a901e03d5a753b0989:0",
    "name": "ownershipTest1",
    "meta_hash": "c695c4d63e2ce368b87bd4ca6610bf3bc48a701f6942a8eb5647b0405e518eaf",
    "asset_id": "923eba65b616fa27f37dae2636ede8a4ee8a1dbda0baa644f56897a1e9d69283",
    "asset_type": "COLLECTIBLE",
    "output_index": 0,
    "version": 0
  },
  "amount": "1",
  "lock_time": 0,
  "relative_lock_time": 0,
  "script_version": 0,
  "script_key": "025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211",
  "script_key_is_local": true,
  "asset_group": null,
  "chain_anchor": {
    "anchor_tx": "0200000000010189093b755a3de001a9c407b1494607ad2c06c1b20bb1a091f8abc4741136f4310000000000ffffffff02e803000000000000225120b7ac2e14d2cb329879f9075d8dbf1167545d62de0e8458dbe74e04fe99dbc3e04620000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602473044022022e93518c67f725df1fd53664c09fd5525c1396cf350a4006f6ebdcc47bfaece0220198e782eb3a4d47170a7abe76f3e91bce78b70f2bcfdafd2d3902a9c576ed521012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000",
    "anchor_block_hash": "0000000000000000000000000000000000000000000000000000000000000000",
    "anchor_outpoint": "d19ef276802f039570682cd15afcb8d1455632fdf818990fa28e0acf503b1ab1:0",
    "internal_key": "026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6",
    "merkle_root": "0970af3b96a1e20c2576b858f277226cf4da6a4d0c88944e06478198b834b253",
    "tapscript_sibling": "",
    "block_height": 0
  },
  "prev_witnesses": [],
  "is_spent": false,
  "lease_owner": "",
  "lease_expiry": "0",
  "is_burn": false
}
```

## Parameters

```json
{
  "asset_id": "923eba65b616fa27f37dae2636ede8a4ee8a1dbda0baa644f56897a1e9d69283",
  "script_key": "025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211",
  "outpoint": {
    "txid": "b11a3b50cf0a8ea20f9918f8fd325645d1b8fc5ad12c687095032f8076f29ed1",
    "output_index": 0
  }
}
```

For outpoint, you need to use the chain_anchor.anchor_outpoint value,
so `d19ef276802f039570682cd15afcb8d1455632fdf818990fa28e0acf503b1ab1:0`, but parse it into the two components, then
byte-reverse the first part. So it would become d19ef276802f039570682cd15afcb8d1455632fdf818990fa28e0acf503b1ab1 , using
this example code in JavaScript:

```javascript
var s = "6db79f5af2ba65bfb4044ced690f3acb4a791a6fc6a7450664e15559ad770b90";
s.match(/[a-fA-F0-9]{2}/g).reverse().join('')
```

## Getting the proof

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./src/main/resources/tapd/admin-local.macaroon)" \
        --data @tmp.json \
        --insecure https://localhost:8089/v1/taproot-assets/wallet/ownership/prove \
        | jq
```

I get:

```json
{
  "proof_with_witness": "54415050000400000000022489093b755a3de001a9c407b1494607ad2c06c1b20bb1a091f8abc4741136f4310000000004500000a020dcacdfae754937ce0a645558cb878d0c48269fd7b2e7c34a1b00000000000000d546892d2923ea70bbc449a2bb2a6e35e03cb1565860913df904451bd4ec88b94137ce65b575211905ac318906f60200000000010189093b755a3de001a9c407b1494607ad2c06c1b20bb1a091f8abc4741136f4310000000000ffffffff02e803000000000000225120b7ac2e14d2cb329879f9075d8dbf1167545d62de0e8458dbe74e04fe99dbc3e04620000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602473044022022e93518c67f725df1fd53664c09fd5525c1396cf350a4006f6ebdcc47bfaece0220198e782eb3a4d47170a7abe76f3e91bce78b70f2bcfdafd2d3902a9c576ed521012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000008fd0123097543cbfcdfc32dde5feef08054cc11e8ac16b17c631f0142be601977a41a8e2c22580afb8b224ce9b349be2df0e2f243df0f5ac8557b2734c9b0ddc207d1d5a5976f9d51c87b3435e7f26f39d76cb08a7e2915762bd4eb1b30582ef9dcfe9ad06c7789cb886fc4d7bf557ee35bb2a0bd22a31c275f669584e17ccc2f1a8dd8a3fbdd1dfff9dfd7d231c95d04046b73df3101c5eac4a0495fe4cd178fd9a7e14abf333a98da091783550bdc9cbfdaae8016379296e255c01ad6a2964483f90819dc12e8eae102dbe1e8abab44081df78f8e684e7a3d2c50dfc54a6f5dc6f1dc69e564a9bbfe3a75cc1bf38e22b9898b65c8217babf1a5c2df06589d474d83585799cea7694d5ef77c86ed9190cd0879a9752aa3025a0335442f3aae9ca7584c0da8010af5000100025889093b755a3de001a9c407b1494607ad2c06c1b20bb1a091f8abc4741136f431000000000e6f776e6572736869705465737431c695c4d63e2ce368b87bd4ca6610bf3bc48a701f6942a8eb5647b0405e518eaf00000000010401010601010b690167016500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0200001021025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d2110c9f0004000000000221026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6037401490001000220923eba65b616fa27f37dae2636ede8a4ee8a1dbda0baa644f56897a1e9d6928304220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff022700010002220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0d30012e00040000000102210264e26d9bcccf4e442e08a8bab1fc2a0b64fcb83b34cbba1fe4b0404c3fd1109805030401011113000100020e6f776e6572736869705465737431154201400f15185ed15447979406f20a861c5c208f23159e88057edbc14f3f2ea9177536e411c80063b9507e7c3167f50190386c28c9a77ff79cb14a0d47aaa5143ee57816040027572f175889093b755a3de001a9c407b1494607ad2c06c1b20bb1a091f8abc4741136f431000000000e6f776e6572736869705465737431c695c4d63e2ce368b87bd4ca6610bf3bc48a701f6942a8eb5647b0405e518eaf0000000001"
}
```

If I decode it :

```bash
    curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./src/main/resources/tapd/admin-local.macaroon)" \
            --data @decode-proof-request.json \
            --insecure https://localhost:8089/v1/taproot-assets/proofs/decode | \
            jq 
```

## Validating the proof

You can do it with this
method: https://lightning.engineering/api-docs/api/taproot-assets/asset-wallet/verify-asset-ownership/index.html