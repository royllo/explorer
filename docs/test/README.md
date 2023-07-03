# Test organization

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
docker exec -it -u tap <containder_id> \
                tapcli assets mint \
                --type normal \
                --name roylloCoin \
                --supply 999 \
                --meta_bytes "Test by Royllo" \
                --enable_emission true
```

You will get this:

```json
{
  "batch_key": "033f2778f288630a6eef31712144b0074be7a5d23a9ede527b5aac3f15e22aee2e"
}
```

To emit the Bitcoin transaction creating our asset, run:

```bash
docker exec -it <containder_id> \
                tapcli assets mint finalize
```

### View asset

```bash
docker exec -it <containder_id> \
                tapcli assets list
```

Or, with curl:

TODO: fix this.

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
                "genesis_point": "e8032ca32339e6a0b062ed3f810f8c26c7dc77bfa4c2023b833685b15c1e7d80:1",
                "name": "roylloCoin",
                "meta_hash": "1cda0bc24fc37cee8702a1b478c834854d74741158770b8b75fe213b61a914e9",
                "asset_id": "313ef582138587c724400c9a6d1a9910d413a44a321f304802e147ab1cba8c39",
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
                "tweaked_group_key": "03bd07a72b305e74c1376937f15544e2f346c60bcfd304357bb877212c0ab3483f",
                "asset_id_sig": "01085efdfe97853875e64e3da374328679addf47a851d9b5357606c445d0f25d0f45cb2182fcbd7f5e0a18f09a71a59acf303120f408fad154762179f4954d4e"
            },
            "chain_anchor": {
                "anchor_tx": "02000000000101807d1e5cb18536833b02c2a4bf77dcc7268c0f813fed62b0a0e63923a32c03e80100000000ffffffff02e80300000000000022512070baf051d7372efa9091ee533213a850efdd288bd56e9348e46c951ed380cb7be614000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602483045022100dc461ee1d34e0d4e662e1415fbd225f8b5cba3b59455891bffab6b2b9330386802202f96886cd40844d7a8cc7ceabb84962c6684f5092c829c95123099e4b6583411012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000",
                "anchor_txid": "5da79b7d8811cea2c008124deefb5b43319d29291b3a0985c68b3dbd74a3b107",
                "anchor_block_hash": "0000000000000000000000000000000000000000000000000000000000000000",
                "anchor_outpoint": "5da79b7d8811cea2c008124deefb5b43319d29291b3a0985c68b3dbd74a3b107:0",
                "internal_key": "026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6",
                "merkle_root": "fb4da5402db81c0f89e9d2c0be93f310a0bdfcf2ba420ef004d96b0a0ca5820c",
                "tapscript_sibling": ""
            },
            "prev_witnesses": [],
            "is_spent": false
        }
    ]
}
```

### Export proof

```bash
docker exec -it <containder_id> \
       tapcli proofs export \
       --asset_id 313ef582138587c724400c9a6d1a9910d413a44a321f304802e147ab1cba8c39 \
       --script_key 025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211
```

Returns:

```
{
    "raw_proof": "0000000001fd04970024807d1e5cb18536833b02c2a4bf77dcc7268c0f813fed62b0a0e63923a32c03e80000000101500080bc2ea631156ad2573ad00af95299218160875b9c97c7969cdb29e4410000000000002720885b5798d576667ec09d4cd6c3a2c60851861e83ccbe14ce13c6aeea47e7b1e49e64facd241969929f7d02f702000000000101807d1e5cb18536833b02c2a4bf77dcc7268c0f813fed62b0a0e63923a32c03e80100000000ffffffff02e80300000000000022512070baf051d7372efa9091ee533213a850efdd288bd56e9348e46c951ed380cb7be614000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602483045022100dc461ee1d34e0d4e662e1415fbd225f8b5cba3b59455891bffab6b2b9330386802202f96886cd40844d7a8cc7ceabb84962c6684f5092c829c95123099e4b6583411012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000003e2072c60e5a4a177ad4dc4d39f6c2444aaae6d82deaad31c99d65d4828fa031d0aa19b733e5a09a1504ab3afaaff6a72f6971822193a14d0d880265c3ac4c0ac24fefd127fd2c993738f46e946898ff7d251ff92c176a6d0e560e0c966987a50be9e879be1119c78fe56b798390ece15a06dd96825fa15ec6d45c3f21fe8007450a12ac4bd7609fa94969eb8a23b0c62494b9a261c173c05abfadab08e5293a958a57408d183623e6e8ec0a40a17e6e37b4f44dab4110363e4c606539c637ca4c0aa416f709932e753f8c2778bf3d70bdb9b5cbe86a85cbff4b14672d7431d971a506304fd01560001000154807d1e5cb18536833b02c2a4bf77dcc7268c0f813fed62b0a0e63923a32c03e8000000010a726f796c6c6f436f696e1cda0bc24fc37cee8702a1b478c834854d74741158770b8b75fe213b61a914e900000000000201000303fd03e70669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d2110a6103bd07a72b305e74c1376937f15544e2f346c60bcfd304357bb877212c0ab3483f01085efdfe97853875e64e3da374328679addf47a851d9b5357606c445d0f25d0f45cb2182fcbd7f5e0a18f09a71a59acf303120f408fad154762179f4954d4e059f0004000000000121026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae60274004900010001200ba7e896dcaae3f7af36933935427f80c59e6b59e3767bfd1ca91fe935e6ce6f02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0630012e00040000000101210264e26d9bcccf4e442e08a8bab1fc2a0b64fcb83b34cbba1fe4b0404c3fd1109803030201010813000100010e5465737420627920526f796c6c6fab0ea3caa0ac23c6f1c60b68f5420e8d07e9b0636691fea473539047c434a2e0",
    "genesis_point": ""
}
```

To export to a file, execute this:

```bash
docker exec -it <containder_id> \
                  tapcli proofs export \
                   --asset_id 313ef582138587c724400c9a6d1a9910d413a44a321f304802e147ab1cba8c39 \
                   --script_key 025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211 \
                  --proof_file /tmp/roylloCoin
docker cp <containder_id>:/tmp/roylloCoin .
```

You can use curl to decode the proof:

Take our proof file and decode it with `cat myRoylloCoinProof | base64 -w 0`, put the content of this file
in `raw_proof` field of `raw_myRoylloCoinProof` file.

TODO: fix this.

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @raw_myRoylloCoinProof \
        --insecure https://127.0.0.1:8089/v1/taproot-assets/proofs/decode
```

### Decode proof

You can copy proof inside the container with the following command : `docker cp myRoylloCoinProof.proof cc510ea50846:.`

```
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli proofs decode \
                --proof_file myRoylloCoinProof \
                --proof_at_depth 0
```

An invalid proof will return :

```json
{
  "code": 2,
  "message": "unable to decode proof file: invalid proof file checksum",
  "details": []
}
```
