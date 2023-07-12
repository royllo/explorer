# Database tips

## How to reset the database ?

This script generates a complete script to drop data structure and data:

```sql
SELECT 'DROP TABLE IF EXISTS "' || tablename || '" CASCADE;'
from pg_tables
WHERE schemaname = 'public'
UNION
select 'drop sequence if exists "' || relname || '" cascade;'
from pg_class
where relkind = 'S'
UNION
select format('drop index if exists %I.%I;', schemaname, indexname) as drop_statement
from pg_indexes
where schemaname not in ('pg_catalog', 'pg_toast');
```
