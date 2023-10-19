# trickyRoylloCoin

```bash
docker exec -it 0ca25af25c32 tapcli assets mint \
                --type normal \
                --name trickyRoylloCoin \
                --supply 11000 \
                --meta_bytes "trickyRoylloCoin by Royllo"
```

We emit the transaction.

```bash
docker exec -it 0ca25af25c32 \
                tapcli assets mint finalize
```