# Test organization

## How coins are created (with sashimi)

### Install and start lnd with taro

```
git clone https://github.com/davisv7/sashimi
cd sashimi
docker-compose up
```

### Asset creation

To create a new asset, first, we need to send BTC to our address, we use this [faucet](https://bitcoinfaucet.uo1.net/).

Then we run this command.

```bash
docker exec -it -u tap e72a16604b0d \
                tapcli assets mint \
                --type normal \
                --name myRoylloCoin \
                --supply 999 \
                --meta_bytes "Used by Royllo" \
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
docker exec -it -u tap e72a16604b0d \
                tapcli assets mint finalize
```

### View asset

```bash
docker exec -it -u tap e72a16604b0d \
                tapcli assets list
```

Or, with curl:

TODO: fix this.

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --insecure https://157.230.85.88:8089/v1/taro/assets
```

Result:

```json
{
  "assets": [
    {
      "version": 0,
      "asset_genesis": {
        "genesis_point": "90ad65d59493b0c355d2ee2332c4aef1b6f753bd5c8ec472ebc716c5ffd9017e:1",
        "name": "myRoylloCoin",
        "meta_hash": "7a884076ca7fda0748ea8910cfeb99a8a9c3a27a0a0db5b84fab0133099d3b74",
        "asset_id": "4c172432b59d0209dae9516e7f62ca4f6fd492aee2cc27070569e684aedbbcc9",
        "output_index": 0,
        "version": 0
      },
      "asset_type": "NORMAL",
      "amount": "999",
      "lock_time": 0,
      "relative_lock_time": 0,
      "script_version": 0,
      "script_key": "02a240542998fd2732ec1a31c77e0607daeea25558169c1c622ab403ff2a2b8bf9",
      "script_key_is_local": true,
      "asset_group": {
        "raw_group_key": "02e481a92c187536f3c0b31933e65d095b880bbd4b015ad7ed6d522c0d13ce1cb6",
        "tweaked_group_key": "0322dd3576f27252932878bbd130049284617eb710ed258147a2667e7d17688bcd",
        "asset_id_sig": "4f573eb1a29fb5214e956a16ba15c5efaab4984561f0f3715a3fe71db14e16ee4878f8a7494029a77f8925a43d545c2d62cc1d6f730c5b067d734e373fe8e9db"
      },
      "chain_anchor": {
        "anchor_tx": "020000000001017e01d9ffc516c7eb72c48e5cbd53f7b6f1aec43223eed255c3b09394d565ad900100000000ffffffff02e803000000000000225120ef6aec163307ea5da0044dba98375d9dd0dd1507d77d75fcc3a2d84236cc7eb00bec15000000000022512076c89cd62f417d2d31ea9ae44e118f6468a304236b20340b7c57d3d8ff478fd502473044022073a282ff6242a8a7d85dde6c36da6cdacea9edf8018ea3bf411f3be7c915984202203d4f65dc168ba654b0fcbcf8b8dcdc088b22c3631f2ae784403f111022177b9901210316fae9bca2b19ce1834d348e0cf3f5914c08d2e1299febaca2e1c51a84cfe73800000000",
        "anchor_txid": "f7d73fc2350e2378320d56ae0f1c9ba92be73e92a2c67a47820184b82a54b714",
        "anchor_block_hash": "0000000000000000000000000000000000000000000000000000000000000000",
        "anchor_outpoint": "f7d73fc2350e2378320d56ae0f1c9ba92be73e92a2c67a47820184b82a54b714:0",
        "internal_key": "033f2778f288630a6eef31712144b0074be7a5d23a9ede527b5aac3f15e22aee2e",
        "merkle_root": "c355ad391e2399ab0c9bccc06a8bc5278ecc1654f9fbe5c76087912b876f54b8",
        "tapscript_sibling": null
      },
      "prev_witnesses": [
      ],
      "is_spent": false
    }
  ]
}
```

### Export proof

```bash
docker exec -it -u tap e72a16604b0d \
                  tapcli proofs export \
                  --asset_id 4c172432b59d0209dae9516e7f62ca4f6fd492aee2cc27070569e684aedbbcc9 \
                  --script_key 02a240542998fd2732ec1a31c77e0607daeea25558169c1c622ab403ff2a2b8bf9
```

Returns:

```
{
    "raw_proof": "0000000001fd047800247e01d9ffc516c7eb72c48e5cbd53f7b6f1aec43223eed255c3b09394d565ad90000000010150002092233f25f1b20e84e83e3f45bdd0ed0682c956f8144ecb46ebe51800000000000000ba401537b5c11a435a9e58f7f3b9223c46757ef437f11a59449314520eb26e9da1dc89647f5d211924c4a10102f6020000000001017e01d9ffc516c7eb72c48e5cbd53f7b6f1aec43223eed255c3b09394d565ad900100000000ffffffff02e803000000000000225120ef6aec163307ea5da0044dba98375d9dd0dd1507d77d75fcc3a2d84236cc7eb00bec15000000000022512076c89cd62f417d2d31ea9ae44e118f6468a304236b20340b7c57d3d8ff478fd502473044022073a282ff6242a8a7d85dde6c36da6cdacea9edf8018ea3bf411f3be7c915984202203d4f65dc168ba654b0fcbcf8b8dcdc088b22c3631f2ae784403f111022177b9901210316fae9bca2b19ce1834d348e0cf3f5914c08d2e1299febaca2e1c51a84cfe7380000000003c20676ed2844f16406098d6cca9b1bbc9ab75a3f93c286dc56fe1c066e59a875db2db7fd90e220f287f4a0a0dfcd20d08155eecd8a24800e576dd847cf3dc4ba501e0b4c7e0d89047bbc81982cf843d5bedf44a9fc224d24d5aac4be052f003c962d8639a175a69a6580ea1e78a9c63e8ffd08ddac38669ee88d1ce60ab035cf101faf2f1e0c55ac04b992baa3ee9308a7a155a939a3b5693b4162a4dc26691df0651ea91486fe70f7f3c5a93b11c4fcd714af87077e1850cbebb1164a098ca717d33904fd015800010001567e01d9ffc516c7eb72c48e5cbd53f7b6f1aec43223eed255c3b09394d565ad90000000010c6d79526f796c6c6f436f696e7a884076ca7fda0748ea8910cfeb99a8a9c3a27a0a0db5b84fab0133099d3b7400000000000201000303fd03e7066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102a240542998fd2732ec1a31c77e0607daeea25558169c1c622ab403ff2a2b8bf90a610322dd3576f27252932878bbd130049284617eb710ed258147a2667e7d17688bcd4f573eb1a29fb5214e956a16ba15c5efaab4984561f0f3715a3fe71db14e16ee4878f8a7494029a77f8925a43d545c2d62cc1d6f730c5b067d734e373fe8e9db059f0004000000000121033f2778f288630a6eef31712144b0074be7a5d23a9ede527b5aac3f15e22aee2e0274004900010001205c3abd547987f066a5fe9df9d1968d861d2c1999506c32ae52cd65e067229acb02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0630012e0004000000010121028cb368cf76d47b33984cdabf5d951d9d64226c020b9873e9d69bb1cd831cb4dd03030201010813000100010e5573656420627920526f796c6c6f1d1dde92cc4739e7280087bd0e9eebd3adcc2c9d599e64d0a5f0a9f982e50887",
    "genesis_point": ""
}
```

To export to a file, execute this:

```bash
docker exec -it -u tap e72a16604b0d \
                  tapcli proofs export \
                  --asset_id 4c172432b59d0209dae9516e7f62ca4f6fd492aee2cc27070569e684aedbbcc9 \
                  --script_key 02a240542998fd2732ec1a31c77e0607daeea25558169c1c622ab403ff2a2b8bf9 \
                  --proof_file /tmp/myRoylloCoinProof
docker cp e72a16604b0d:/tmp/myRoylloCoinProof .
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
