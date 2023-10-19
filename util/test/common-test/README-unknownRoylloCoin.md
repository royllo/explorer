# unknownRoylloCoin

```bash
docker exec -it 0ca25af25c32 tapcli assets mint \
                --type normal \
                --name unknownRoylloCoin \
                --supply 12000 \
                --meta_bytes "unknownRoylloCoin by Royllo"
```

We emit the transaction.

```bash
docker exec -it 0ca25af25c32 \
                tapcli assets mint finalize
```