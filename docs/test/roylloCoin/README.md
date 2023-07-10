# Royllo coin

## How coins are created

### Install and start lnd with taro

```
git clone https://github.com/royllo/tade
cd tade
docker-compose up
```

### Asset creation

To create a new asset, first, we need to send BTC to our address, we use this [faucet](https://bitcoinfaucet.uo1.net/).

Then we run this command.

```bash
docker exec -it b350b286ccdf \
                tapcli assets mint \
                --type normal \
                --name roylloCoin \
                --supply 999 \
                --meta_bytes "Royllo coin for testnet" \
                --enable_emission true
```

You will get something like this as a result:

```json
{
  "batch_key": "026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6"
}
```

To emit the Bitcoin transaction creating our asset, run:

```bash
docker exec -it b350b286ccdf \
                tapcli assets mint finalize
```

### View assets

```bash
docker exec -it b350b286ccdf \
                tapcli assets list
```

Or, with curl:

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/assets
```

Result:

```json
{
  "assets": [
    {
      "version": 0,
      "asset_genesis": {
        "genesis_point": "57faede9e107f3af74bb93e730bc1dfd07f81b166a974068f823d30d9eb10111:1",
        "name": "roylloCoin",
        "meta_hash": "0c482467dfb29000804e044c4c6044b487c0280082002d4611a8ba7f22703d63",
        "asset_id": "f9dd292bb211dae8493645150b36efa990841b11038d026577440d2616d1ec32",
        "output_index": 0,
        "version": 0
      },
      "asset_type": "NORMAL",
      "amount": "999",
      "lock_time": 0,
      "relative_lock_time": 0,
      "script_version": 0,
      "script_key": "025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211",
      "script_key_is_local": true,
      "asset_group": {
        "raw_group_key": "033b8449aae83eb0ed04f7952108637d078836bb5d3353a33b6486248b401f60da",
        "tweaked_group_key": "02260e9ffbe7fdabe746b4ba4c3b86c8f237d7946f116949e93db77d5e0357c13d",
        "asset_id_sig": "d0d86f5c646fcca990b8ceaf7a480762c92900351ae84e8135740fa92a66c8c0a4e509c8d10df8913924a583d74e9cf25ccd56eb2552c3b92a8c3c9ebe8cca97"
      },
      "chain_anchor": {
        "anchor_tx": "020000000001021101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570100000000ffffffffdc62e81401abfec546a45213c65c9910466f06ea1b831b017c13edfa71d775b10200000000ffffffff02e80300000000000022512040b3ddefc0df09b89fd1379a3bd246bf2827ed461506a5899dc230324d0aa158bc05000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602473044022023f92ca5b7289d5cfb2ae415b91efa8bb9758d97b78b8ad17c81ce43fa9d262a02205c790874cbd4dae139f1d3e2b029bb97c4bf13bff0bc80127dacf9e917a37ed5012102cf948a447b6b49ccf83754b2455b7c6f77a9daeb1bb192f17ae34d3f52182a1a024630430220698a8f0543c97775ece1ad26a63f99d837ff9fd3b136e5c43cc984c37a75b3dd021f4d183f9811ac7a7cde904c795f68f96bda845efb5493a95582b60a93ca55c201210305c9ac7185c67621b7534e700d9592863f07ae5b9add38da9576e4a06a66c10000000000",
        "anchor_txid": "0324ddf7ec79711a340a04e1ee3cee53005e046b4e0de31d498cc586ba9181c7",
        "anchor_block_hash": "0000000000000000000000000000000000000000000000000000000000000000",
        "anchor_outpoint": "0324ddf7ec79711a340a04e1ee3cee53005e046b4e0de31d498cc586ba9181c7:0",
        "internal_key": "026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6",
        "merkle_root": "cd773bc1a59f3b641e819b51944d1bcf10e4676cba0620ebe3b064c5d0a777da",
        "tapscript_sibling": ""
      },
      "prev_witnesses": [],
      "is_spent": false
    }
  ]
}
```

### Export proof

You will have to wait a bit before the proof is available to export.

```bash
docker exec -it b350b286ccdf \
       tapcli proofs export \
       --asset_id f9dd292bb211dae8493645150b36efa990841b11038d026577440d2616d1ec32 \
       --script_key 025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211
```

Returns:

```
{
    "raw_proof": "0000000001fd051400241101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570000000101500000d6224f34b13ded7327694942bf00a9060938f759a544e98e7b0139120000000000002d2c483ab11c46d36e302cae0e225c790ce201449138186b3dc7e83e72652a594816a764ffff001d274d807202fd0189020000000001021101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570100000000ffffffffdc62e81401abfec546a45213c65c9910466f06ea1b831b017c13edfa71d775b10200000000ffffffff02e80300000000000022512040b3ddefc0df09b89fd1379a3bd246bf2827ed461506a5899dc230324d0aa158bc05000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602473044022023f92ca5b7289d5cfb2ae415b91efa8bb9758d97b78b8ad17c81ce43fa9d262a02205c790874cbd4dae139f1d3e2b029bb97c4bf13bff0bc80127dacf9e917a37ed5012102cf948a447b6b49ccf83754b2455b7c6f77a9daeb1bb192f17ae34d3f52182a1a024630430220698a8f0543c97775ece1ad26a63f99d837ff9fd3b136e5c43cc984c37a75b3dd021f4d183f9811ac7a7cde904c795f68f96bda845efb5493a95582b60a93ca55c201210305c9ac7185c67621b7534e700d9592863f07ae5b9add38da9576e4a06a66c1000000000003c206e47b7ffac0a46c638ebd66bd72c07c93059cb9dcacd5474b4d29bb0f1cc661ef12dc3d91a992c4d4800fadc4593e665783966e9ea18257e7d1d187ee3f70a3a53f660f84796da9dccf2521f2bc0caddb627789062cca22e8de926f1e2327370f38e8899c9da3fcc32ccbccb65ccac88e253398c0d447323a456813bf08f7b22bd1af29b3bec54a12010c5bcd4a545e0bc461507236adc7fef624b70042f53a0ebf264c2dfde363f7e68820f57767962e412238ce2ee06ace92795f496ee1c5eb2e04fd015600010001541101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa57000000010a726f796c6c6f436f696e0c482467dfb29000804e044c4c6044b487c0280082002d4611a8ba7f22703d6300000000000201000303fd03e70669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d2110a6102260e9ffbe7fdabe746b4ba4c3b86c8f237d7946f116949e93db77d5e0357c13dd0d86f5c646fcca990b8ceaf7a480762c92900351ae84e8135740fa92a66c8c0a4e509c8d10df8913924a583d74e9cf25ccd56eb2552c3b92a8c3c9ebe8cca97059f0004000000000121026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae60274004900010001209e4f59a9e6c363a266472043f21f2f11b0b7f206f2abd8760d555832f16cf63f02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0630012e00040000000101210264e26d9bcccf4e442e08a8bab1fc2a0b64fcb83b34cbba1fe4b0404c3fd110980303020101081c0001000117526f796c6c6f20636f696e20666f7220746573746e657401bc6c08537fd418062422c4c2803bca5cc7edba3fd5693f5d3f6370a45d0d7d",
    "genesis_point": ""
}
```

To export the proof to a file, execute this:

```bash
docker exec -it b350b286ccdf \
                  tapcli proofs export \
                  --asset_id f9dd292bb211dae8493645150b36efa990841b11038d026577440d2616d1ec32 \
                  --script_key 025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211 \
                  --proof_file /tmp/roylloCoinProof
docker cp b350b286ccdf:/tmp/roylloCoinProof .
```

You can decode the exported proof with the command:

```bash
docker exec -it b350b286ccdf \
                tapcli proofs decode \
                --proof_file /tmp/roylloCoinProof
```

You can use curl to decode the proof but you have to pass several parameters as json:

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-parameter.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode
```

