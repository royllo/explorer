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

## How coins are created

### Install and start lnd with taro

```
git clone https://github.com/royllo/lnd-taro-with-docker.git
cd lnd-taro-with-docker
docker-compose up
```

### Asset creation

```
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli assets mint \
                --type normal \
                --name activeRoylloCoin \
                --supply 1003 \
                --meta "Used by Royllo" \
                --skip_batch
```

### View asset

```
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli assets list
```

### Export proof

```
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli proofs \
                export  --asset_id 1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413 \
                        --script_key 02576e6cf95f0d7724f2e17afcd74a690231bf4e1ecb1963229af3fe33edcd58ca \
                        --proof_file activeRoylloCoin-3.proof
```

### Decode proof

```
docker exec -it lnd-taro-with-docker_taro_1 \
                /bin/tarocli proofs decode \
                --proof_file activeRoylloCoin-2.proof \
                --proof_index 0
```

AN invalid proof will return :

```json
{
  "code": 2,
  "message": "unable to decode proof file: invalid proof file checksum",
  "details": []
}
```