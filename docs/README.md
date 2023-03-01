For our test, we used this
address [tb1q9ep90x63j3n5dqqyn3jjf8kmpzf65tttupudyg](https://mempool.space/testnet/address/tb1q9ep90x63j3n5dqqyn3jjf8kmpzf65tttupudyg).

After starting our `docker-compose up` and waiting the `SRVR: Taro Daemon fully active!` message to appear, we created a
new coin with the command:

```bash
docker exec -it lnd-taro-with-docker_taro_1 /bin/tarocli assets mint --type normal --name roylloCoin --supply 1000 --meta "Royllo coin" --skip_batch
```

You can see its creation with the command:

```bash
docker exec -it lnd-taro-with-docker_taro_1 /bin/tarocli assets list
```

It will display something like this:

```json
{
  "assets": [
    {
      "version": 0,
      "asset_genesis": {
        "genesis_point": "1f14bc82086f238c6d32dbe33e49fb9d96c11f74d3fcbb83fd735915d360ef3f:0",
        "name": "roylloCoin",
        "meta": "526f796c6c6f20636f696e",
        "asset_id": "311a7921e76bcd9abc4aae792a36d0f1bca98148ba3fb79603a8275b9b70a6ff",
        "output_index": 0,
        "genesis_bootstrap_info": "3fef60d3155973fd83bbfcd3741fc1969dfb493ee3db326d8c236f0882bc141f000000000a726f796c6c6f436f696e0b526f796c6c6f20636f696e0000000000",
        "version": 0
      },
      "asset_type": "NORMAL",
      "amount": "1000",
      "lock_time": 0,
      "relative_lock_time": 0,
      "script_version": 0,
      "script_key": "022e62cc964416daf4d03ac8891c8b32fdd72d0510e5d767d984bf598091d2bd53",
      "asset_group": null,
      "chain_anchor": {
        "anchor_tx": "020000000001013fef60d3155973fd83bbfcd3741fc1969dfb493ee3db326d8c236f0882bc141f0000000000ffffffff02e803000000000000225120afe6c3a39beff80d207fb2f7af0076d2ced4e60f5fae85951d6f8628b467becbbb08000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e02483045022100ebcc3c960e487edd9a30aacd86db7d9ceaf65a603e641eb58931cb0237fb37da02207aea3a99106666d1276d82a4324f2d39b84a86496faa20b86a32de9db1d8a01c012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000",
        "anchor_txid": "4155275d5468dc2f116e3ea4aaddc4783a559933c7bef08fd3029fde40daf26d",
        "anchor_block_hash": "dac3d084990912c591a8f57753d85a1f6584cdb7c5b981410000000000000000",
        "anchor_outpoint": "4155275d5468dc2f116e3ea4aaddc4783a559933c7bef08fd3029fde40daf26d:0",
        "internal_key": "02bf9d0eac1be77456ced581721d9f8837b42346173556c418794e0347e08f8171"
      },
      "prev_witnesses": [
      ]
    }
  ]
}
```

You can then export this proof:

```bash
docker exec -it lnd-taro-with-docker_taro_1 /bin/tarocli proofs export --asset_id 311a7921e76bcd9abc4aae792a36d0f1bca98148ba3fb79603a8275b9b70a6ff --script_key 022e62cc964416daf4d03ac8891c8b32fdd72d0510e5d767d984bf598091d2bd53
```

You will get:

```json
{
    "raw_proof": "0000000001fd03ab00243fef60d3155973fd83bbfcd3741fc1969dfb493ee3db326d8c236f0882bc141f0000000001500000602096e1775c63638141b61f7ea2f0fd37b0a655fe587c8762650400000000000000bb195bc2ff46334651c66abc7dbdf022820921a283a4b116102b5d8d03aa65e36451f3639cde2c195e75a84f02eb020000000001013fef60d3155973fd83bbfcd3741fc1969dfb493ee3db326d8c236f0882bc141f0000000000ffffffff02e803000000000000225120afe6c3a39beff80d207fb2f7af0076d2ced4e60f5fae85951d6f8628b467becbbb08000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e02483045022100ebcc3c960e487edd9a30aacd86db7d9ceaf65a603e641eb58931cb0237fb37da02207aea3a99106666d1276d82a4324f2d39b84a86496faa20b86a32de9db1d8a01c012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000003c2069767fe9f42f6cc58ae70459df279d7add8c8af88aa6acf6f6e3990f6cc9c3818ffb078cb5ee4d9f16f7a455faf4c2b717b09aba54923775cc92e20e7483db45282d44e411188830e6ee11d32da81d7deffa5ed1db0dd7dbe22cda1cdd173a58ad290e3800d78b8e8f05c957a6de3b458bdcaf00dda7928579b2cd41c08081be95972e60c69f9f4f092773be60bed5c967298d4e043457ebd3b7e926a88865f7509118d11df45e4bce296ca837e56a81b6ae6092c421ca0784b926fce2151cf661e04df00010001403fef60d3155973fd83bbfcd3741fc1969dfb493ee3db326d8c236f0882bc141f000000000a726f796c6c6f436f696e0b526f796c6c6f20636f696e00000000000201000303fd03e80669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921022e62cc964416daf4d03ac8891c8b32fdd72d0510e5d767d984bf598091d2bd53059f000400000000012102bf9d0eac1be77456ced581721d9f8837b42346173556c418794e0347e08f8171027400490001000120311a7921e76bcd9abc4aae792a36d0f1bca98148ba3fb79603a8275b9b70a6ff02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffa561068fba3926c352a2dad8d6e84314460bf6f30a3e1d0306a5cf56fddf2368",
    "genesis_point": ""
}
```

You can create an address:

```bash
docker exec -it lnd-taro-with-docker_taro_1 /bin/tarocli addrs new --genesis_bootstrap_info 3fef60d3155973fd83bbfcd3741fc1969dfb493ee3db326d8c236f0882bc141f000000000a726f796c6c6f436f696e0b526f796c6c6f20636f696e0000000000 --amt 100
```

and you will get:

```json
{
    "encoded": "tarotb1qqqsqqjq8lhkp5c4t9elmqamlnfhg87pj6wlkjf7u0dnymvvydhs3q4uzs0sqqqqqq98ymmed3kx7sm0d9hqk5n009kxcmeqvdhkjmsqqqqqqqqyyyppq2nuy6dlvx6c0338sgrdq4pnlgymn7lrp4sc5txxrp5xyt64rrcxyypnx6pse6kmplrlvl8ktvdhn5dhm3hv60gnlu403d82de73fnhplesgq9jqu8zuyz",
    "asset_id": "311a7921e76bcd9abc4aae792a36d0f1bca98148ba3fb79603a8275b9b70a6ff",
    "asset_type": "NORMAL",
    "amount": "100",
    "group_key": null,
    "script_key": "02102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f",
    "internal_key": "03336830ceadb0fc7f67cf65b1b79d1b7dc6ecd3d13ff2af8b4ea6e7d14cee1fe6",
    "taproot_output_key": "32c1d8ce773cc6fae4d528e8874dd1e5fa3af794c79b383e72cf52b6d6af384e"
}
```
