# Asset page design

## Test data

For one asset, we can have several proofs.

We have three proofs: `testCoinProof1`, `testCoinProof2`, `testCoinProof3`. When we decode a proof file, there could be
several proofs; The index depth of the decoded proof, with 0 being the latest proof.

### testCoinProof1

Here, our proof files contains only one proof (`proof_at_depth`: 0, `number_of_proofs`: 1) and `amount` = 1 000.

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof1.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

### testCoinProof2

Here, our proof file contains two proof (`proof_at_depth`: 1, `number_of_proofs`: 2).

Proof 1:

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof2-0.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

Proof 2:

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof2-1.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

### testCoinProof3

Here, our proof file contains two proof (`proof_at_depth`: 1, `number_of_proofs`: 2).

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof3-0.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

```bash
curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./admin.macaroon)" \
        --data @REST-decode-testCoin-proof3-1.json \
        --insecure https://157.230.85.88:8089/v1/taproot-assets/proofs/decode | jq
```

### Data we have

Header :

- Asset name
- Asset id
- Type.

Tab `Asset genesis`:

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