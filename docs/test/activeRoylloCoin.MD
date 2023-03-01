# activeRoylloCoin

## Asset

```json
{
  "version": 0,
  "asset_genesis": {
    "genesis_point": "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1",
    "name": "activeRoylloCoin",
    "meta": "5573656420627920526f796c6c6f",
    "asset_id": "1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413",
    "output_index": 1,
    "genesis_bootstrap_info": "39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100",
    "version": 0
  },
  "asset_type": "NORMAL",
  "amount": "1003",
  "lock_time": 0,
  "relative_lock_time": 0,
  "script_version": 0,
  "script_key": "024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc",
  "asset_group": null,
  "chain_anchor": {
    "anchor_tx": "0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d600000000",
    "anchor_txid": "60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7",
    "anchor_block_hash": "000000000000002366fc3e4cc4074a94529e09858b6585004a0056e47e08c0c9",
    "anchor_outpoint": "60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7:1",
    "internal_key": "03bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e962401429"
  },
  "prev_witnesses": [
  ]
}
```

## Just after creation

###  Raw proof 1

```
0000000001fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576f
```

### Decoded proof

```json
{
    "decoded_proof": {
        "proof_index": "0",
        "number_of_proofs": "1",
        "asset": {
            "version": 0,
            "asset_genesis": {
                "genesis_point": "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1",
                "name": "activeRoylloCoin",
                "meta": "5573656420627920526f796c6c6f",
                "asset_id": "1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413",
                "output_index": 1,
                "genesis_bootstrap_info": "39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100",
                "version": 0
            },
            "asset_type": "NORMAL",
            "amount": "1003",
            "lock_time": 0,
            "relative_lock_time": 0,
            "script_version": 0,
            "script_key": "024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc",
            "asset_group": null,
            "chain_anchor": {
                "anchor_tx": "0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d600000000",
                "anchor_txid": "60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7",
                "anchor_block_hash": "000000000000002366fc3e4cc4074a94529e09858b6585004a0056e47e08c0c9",
                "anchor_outpoint": "60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7:1",
                "internal_key": "03bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e962401429"
            },
            "prev_witnesses": [
            ]
        },
        "tx_merkle_proof": "05e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c761511",
        "inclusion_proof": "000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
        "exclusion_proofs": [
        ]
    }
}
```

## Creating an address

```bash
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli addrs new \
                --genesis_bootstrap_info 39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100 \
                --amt 100
```

Results in this:
```json
{
    "encoded": "tarotb1qqqsqqjf89ypudw2pzxejxjwd4efdv3vv5z5tu9mlcy9p5awfz3pgvv0sndsqqqqqygxzcm5d9mx25n009kxcm6rda5kurj4wdjkggrz0ys9ymmed3kx7qqqqqqsqpppqftkum8etuxhwf8ju9a0e462dyprr06wrm93jceznteluvlde4vv5p3pqvzprk6dqgag64tq0lku2ck4rxcc2jhhw5keu8cqy7fpxwqdgw0gszqpvshvqans",
    "asset_id": "1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413",
    "asset_type": "NORMAL",
    "amount": "100",
    "group_key": null,
    "script_key": "02576e6cf95f0d7724f2e17afcd74a690231bf4e1ecb1963229af3fe33edcd58ca",
    "internal_key": "030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e88",
    "taproot_output_key": "9588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3"
}
```

## Sending asset to that addresse

```bash
docker exec -it lnd-taro-with-docker_taro_1 \
                    /bin/tarocli assets send -–addr `` tarotb1qqqsqqjf89ypudw2pzxejxjwd4efdv3vv5z5tu9mlcy9p5awfz3pgvv0sndsqqqqqygxzcm5d9mx25n009kxcm6rda5kurj4wdjkggrz0ys9ymmed3kx7qqqqqqsqpppqftkum8etuxhwf8ju9a0e462dyprr06wrm93jceznteluvlde4vv5p3pqvzprk6dqgag64tq0lku2ck4rxcc2jhhw5keu8cqy7fpxwqdgw0gszqpvshvqans
```

We generate a proof with the previous scriptkey:
```
docker exec -it lnd-taro-with-docker_taro_1 \
                    /bin/tarocli proofs export \
                    --asset_id 1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413 \
                    --script_key 02576e6cf95f0d7724f2e17afcd74a690231bf4e1ecb1963229af3fe33edcd58ca
```
