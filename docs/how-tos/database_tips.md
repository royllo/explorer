# Database tips

## How to reset the database ?

```sql
DROP TABLE IF EXISTS "databasechangeloglock" CASCADE;
DROP TABLE IF EXISTS "databasechangelog" CASCADE;
DROP TABLE IF EXISTS "bitcoin_transaction_outputs" CASCADE;
DROP TABLE IF EXISTS "assets" CASCADE;
DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS "requests" CASCADE;
DROP TABLE IF EXISTS "requests_add_proof" CASCADE;
DROP TABLE IF EXISTS "requests_add_asset_meta_data" CASCADE;
DROP TABLE IF EXISTS "proofs" CASCADE;
```

This script is generated with the command:

```sql
SELECT 'DROP TABLE IF EXISTS "' || tablename || '" CASCADE;'
from pg_tables
WHERE schemaname = 'public';
```

You can drop sequences with the command:

```sql
select 'drop sequence if exists "' || relname || '" cascade;'
from pg_class
where relkind = 'S';
```

You can drop indexes with the command:

```sql
select schemaname,
       indexname,
       tablename,
       format('drop index %I.%I;', schemaname, indexname) as drop_statement
from pg_indexes
where schemaname not in ('pg_catalog', 'pg_toast');
```
