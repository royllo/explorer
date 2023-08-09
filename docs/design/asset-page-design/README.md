# Asset page design

## Test data

For one asset, we can have several proofs.

We have three proofs: `testCoinProof1`, `testCoinProof2`, `testCoinProof3`.

We decode it with the commands:

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof1.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof2.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof3.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

### Data we have

Header :

- Asset name
- Asset id
- Type.

Tab `Asset`:

- Asset id.
- Genesis point.
- Meta hash.
- Name.
- Output index.
- Version

Tab `Asset group`:

- asset_id_sig
- raw_group_key
- tweaked_group_key

Tab `Chain anchors`, display several anchors:

- anchor_block_hash
- anchor_outpoint
- anchor_tx
- anchor_txid
- internal_key
- merkle_root
- tapscript_sibling

Tab `proofs`, display several proofs:

- First and last characters of the proof.
- Copy button.

Tab `Owner`: display information about how you can claim ownership of the asset.

I don't know:

- amount.