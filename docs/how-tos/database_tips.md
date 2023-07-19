# Database tips

## How to reset the database ?

This script generates a complete script to drop data structure and data:

```sql
SELECT 'DROP TABLE IF EXISTS "' || tablename || '" CASCADE;'
from pg_tables
WHERE schemaname = 'public'
```
