# setOfRoylloNFT

NFT1

```bash
docker exec -it 9e26560b40cf tapcli assets mint \
                --type collectible \
                --name setOfRoylloNFT1 \
                --supply 1 \
                --meta_bytes "setOfRoylloNFT1 by Royllo" \
                --enable_emission \
                --group_anchor setOfRoylloNFT
```

NTF2

```bash
docker exec -it 9e26560b40cf tapcli assets mint \
                --type collectible \
                --name setOfRoylloNFT2 \
                --supply 1 \
                --meta_bytes "setOfRoylloNFT2 by Royllo" \
                --enable_emission \
                --group_anchor setOfRoylloNFT
```

We emit the transaction.

```bash
docker exec -it 9e26560b40cf \
                tapcli assets mint finalize
```