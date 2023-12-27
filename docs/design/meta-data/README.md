# Getting meta-data from the API

Let's take an asset: 5630236058450d73ef22ca1e4bea24b5fc05f98688fa6502775f79275445222f
We download the proof from explorer.

The proof is named: `punk-proof`

You then insert it in the request: `punk-request.json`

We then call the decode method:

    ```bash
        curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 IdeaProjects/royllo/explorer/util/test/common-test/src/main/resources/tapd/admin-mainnet.macaroon)" \
                --data @punk-request.json \
                --insecure https://universe.royllo.org:8089/v1/taproot-assets/proofs/decode | jq 
    ```

We retrieve the meta field and we do `xxd -r -p` on it

## Getting meta data

### Getting issuance proof

Getting issuance proof of AdamCoin (an asset):

```bash
curl --insecure https://universe.tiramisuwallet.com:8089/v1/taproot-assets/universe/leaves/asset-id/3b7693c532a59186a56c25b39328bb1801b052ca960a7effd25724f3074eeda9?proof_type=PROOF_TYPE_ISSUANCE | jq .leaves[0].issuance_proof  | tr -d '"'
```

Getting issuance proof of tiramisuwallet (an NFT):

```bash
curl --insecure https://universe.tiramisuwallet.com:8089/v1/taproot-assets/universe/leaves/asset-id/74b0bcd927fd5a8b296c5e859293639578c459611419988e5a2cab4fff18f7d3?proof_type=PROOF_TYPE_ISSUANCE | jq .leaves[0].issuance_proof  | tr -d '"'
```

### Decoding proof

Decoding AdamCoin proof:

```bash
    curl    --header "Grpc-Metadata-macaroon: $(xxd -ps -u -c 1000 ./src/main/resources/tapd/admin-mainnet.macaroon)" \
            --data @adamCoin-request.json \
            --insecure https://universe.royllo.org:8089/v1/taproot-assets/proofs/decode
```

We can decode the meta with: `xxd -r -p`