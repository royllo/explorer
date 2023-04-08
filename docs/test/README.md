# Test organization

## Test cases

- Case 1: The coin and its proof exists in database - We add the same proof.
- Case 2: The coin and its proof exists in database - We add a new proof.
- Case 3: The coin does not exist in database - We add a proof to create it.
- Case 4: The coin does not exist in database - We add two proofs.

## Coin list

| Coin name         | Found in                              | Test case                                               |
|-------------------|---------------------------------------|---------------------------------------------------------|
| myRoylloCoin      | Production initialization script      | Default example displayed on the search engine web page |
| knownRoylloCoin   | Test script in explorer-core          | Case 1 & 2                                              |
| unknownRoylloCoin | DecodedResponse mock in explorer-core | Case 3                                                  |
| activeRoylloCoin  | DecodedResponse mock in explorer-core | Case 4                                                  |

## How coins are created (docker)

### Install and start lnd with taro

```
git clone https://github.com/royllo/lnd-taro-with-docker.git
cd lnd-taro-with-docker
docker-compose up
```

### Asset creation

To crate a new asset, first, we need to send BTC to our address `tb1q9ep90x63j3n5dqqyn3jjf8kmpzf65tttupudyg`, we use
this [faucet](https://bitcoinfaucet.uo1.net/).

Then we run this command.

```bash
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli assets mint \
                --type normal \
                --name myRoylloCoin \
                --supply 999 \
                --meta "Used by Royllo" \
                --skip_batch
```

### View asset

```bash
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli assets list
```

Or, with curl:

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --insecure https://157.230.85.88:8089/v1/taro/assets
```

Result:

```json
{
  "version": 0,
  "asset_genesis": {
    "genesis_point": "ac7422f6dbdd2bc5acde81a1e3743627a72fb53790694e13d48dfef7d1b6d688:0",
    "name": "myRoylloCoin",
    "meta": "5573656420627920526f796c6c6f",
    "asset_id": "18c4eaca8905cad3ae027494b8c70a085e9452226ddea8eacfdaf6cb88431c12",
    "output_index": 0,
    "genesis_bootstrap_info": "88d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac000000000c6d79526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000000",
    "version": 0
  },
  "asset_type": "NORMAL",
  "amount": "999",
  "lock_time": 0,
  "relative_lock_time": 0,
  "script_version": 0,
  "script_key": "02102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f",
  "asset_group": null,
  "chain_anchor": {
    "anchor_tx": "0200000000010188d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac0000000000ffffffff02e80300000000000022512068b8ab655c6f3edfc0b241a3c560355146ed6bbf92514c09b10a0c86ac140e23910a000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0247304402201829c56dba3c2ab1cd85de1fe3d7bfec8dffc2b2d4aac8c362cae25052a420290220506b92ca45a514a8b2daabb1eb8adf5283ab29b382d0331c9c884f8c89dd830f012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000",
    "anchor_txid": "57faede9e107f3af74bb93e730bc1dfd07f81b166a974068f823d30d9eb10111",
    "anchor_block_hash": "0000000000000000000000000000000000000000000000000000000000000000",
    "anchor_outpoint": "57faede9e107f3af74bb93e730bc1dfd07f81b166a974068f823d30d9eb10111:0",
    "internal_key": "02bf9d0eac1be77456ced581721d9f8837b42346173556c418794e0347e08f8171"
  },
  "prev_witnesses": [
  ]
}
```

### Export proof

**It may take time to be able to export a proof**

```bash
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli proofs \
                export  --asset_id 18c4eaca8905cad3ae027494b8c70a085e9452226ddea8eacfdaf6cb88431c12 \
                        --script_key 02102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f
```

Returns:

```
{
    "raw_proof": "0000000001fd03f1002488d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac00000000015000000020dbc0a8738a77d0b1f29df61820e015784d87b13f508d07af1200000000000000e9a0e62808024419f29e12075ee7192283d45787ba4e510dcc4325c4f13e2da617252c64ffff001dee9af8c502ea0200000000010188d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac0000000000ffffffff02e80300000000000022512068b8ab655c6f3edfc0b241a3c560355146ed6bbf92514c09b10a0c86ac140e23910a000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0247304402201829c56dba3c2ab1cd85de1fe3d7bfec8dffc2b2d4aac8c362cae25052a420290220506b92ca45a514a8b2daabb1eb8adf5283ab29b382d0331c9c884f8c89dd830f012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000003fd0102083c7c4198be914f1cab7319592f0249d727fa87890ec4185b8b7881d777158d0af976bfe9e708afddbef3616fe086f90e0879295f231ac2c6d5be387ba682903bc9f67bd23cff72af821bc6feb5995b83b603aba9074ce1c74a4e13a45f108713102ce2cdd7590df19cc6c9b0f631097a2dedec3e30c1633300ea02e179eea39ef366c1a3711657d6302e516e3218b8de4a0089ca0f92621a8a49a8790dc9a1deba788b11327b109130ca4dede1a29d6ac6c03a86454199c5bc6e8a74d32df7f93d7b24f5dec13ec57f232b95d712d5947b04785cac15d00e4ea9ae6e0ffd9b3361cab39effb1b072f0918fd3ea9e9e0ae35ee61ed51e9d09548c619f06d4caa8c104e4000100014588d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac000000000c6d79526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000000000201000303fd03e7066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f059f000400000000012102bf9d0eac1be77456ced581721d9f8837b42346173556c418794e0347e08f817102740049000100012018c4eaca8905cad3ae027494b8c70a085e9452226ddea8eacfdaf6cb88431c1202220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0ea5f7971d2fe0f27d8975f656332a5f6bf0176b998a52dedaedf2ffbaef922f",
    "genesis_point": ""
}
```

To export to a file, execute this:

```bash
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli proofs \
                export  --asset_id 18c4eaca8905cad3ae027494b8c70a085e9452226ddea8eacfdaf6cb88431c12 \
                        --script_key 02102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f \
                        --proof_file myRoylloCoinProof
docker cp cc510ea50846:myRoylloCoinProof .
```

You can use curl to decode the proof:

Take our proof file and decode it with `cat myRoylloCoinProof | base64 -w 0`, put the content of this file
in `raw_proof` field of `raw_myRoylloCoinProof` file.

```bash

curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @raw_myRoylloCoinProof \
        --insecure https://157.230.85.88:8089/v1/taro/proofs/decode
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
