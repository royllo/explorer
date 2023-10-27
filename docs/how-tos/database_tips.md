# Database tips

## How to reset the database ?

This script generates a complete script to drop data structure and data:

```sql
SELECT 'DROP TABLE IF EXISTS "' || tablename || '" CASCADE;'
from pg_tables
WHERE schemaname = 'public'
```

## How to export database data to liquibase

Download [Liquibase](https://www.liquibase.com/download)

Run:

```bash
./liquibase generate-changelog \
            --diff-types="data" \
            --changelog-file="/home/straumat/export.xml" \
            --url=jdbc:postgresql://127.0.0.1/royllo_explorer_database \
            --username=royllo_explorer_username \
            --password=Q5QnE5S%Y]qt7
```