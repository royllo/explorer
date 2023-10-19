# unlimitedRoylloCoin

First emission:

```bash
docker exec -it 0ca25af25c32 tapcli assets mint \
                --type normal \
                --name unlimitedRoylloCoin1 \
                --supply 499 \
                --meta_bytes "unlimitedRoylloCoin emission 1 by Royllo" \
                --enable_emission true
```

We emit the transaction.

```bash
docker exec -it 0ca25af25c32 \
                tapcli assets mint finalize
```

Second emission:

```bash
docker exec -it 0ca25af25c32 tapcli assets mint \
                --type normal \
                --name unlimitedRoylloCoin2 \
                --supply 501 \
                --meta_bytes "unlimitedRoylloCoin emission 2 by Royllo" \
                --enable_emission true \
                --group_key COPY_PASTE_THE_TWEAKED_GROUP_KEY_HERE
```

We emit the transaction.

```bash
docker exec -it 0ca25af25c32 \
                tapcli assets mint finalize
```