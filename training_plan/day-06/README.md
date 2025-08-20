# Day 6: Creating Iceberg Tables

## Topic: Defining Your First Iceberg Table

Now that we have a conceptual understanding of Iceberg, let's get our hands dirty and create our first Iceberg table. We will show examples using both Spark and Flink, our two processing engines.

---

### Option 1: Using Spark SQL

Our `docker-compose.yml` file has already set up a Spark container with the Iceberg catalog pre-configured. You can start the interactive shell with this command:

```bash
docker exec -it spark-iceberg spark-sql
```

#### **Creating a Table**
The syntax for creating an Iceberg table in Spark is as follows:

```sql
CREATE TABLE catalog.ecommerce.customers (
    customer_id BIGINT,
    name STRING,
    email STRING,
    registration_date TIMESTAMP
)
USING iceberg
PARTITIONED BY (days(registration_date));
```
*   `USING iceberg`: This clause is specific to Spark and tells it to use the Iceberg format.
*   `catalog...`: The catalog name `catalog` was pre-configured for you in the `docker-compose.yml` file.

#### **Schema Evolution**
Schema evolution commands are standard SQL and work similarly in Spark:
```sql
-- Add a new column
ALTER TABLE catalog.ecommerce.customers ADD COLUMN new_col STRING;

-- Rename a column
ALTER TABLE catalog.ecommerce.customers RENAME COLUMN new_col TO last_name;

-- Drop a column
ALTER TABLE catalog.ecommerce.customers DROP COLUMN last_name;
```

---

### Option 2: Using Flink SQL

Flink provides an interactive SQL Client very similar to Spark's. You can start it with this command:

```bash
docker exec -it flink sql-client
```

#### **Setting up the Catalog**
Unlike our Spark setup, Flink requires you to configure the connection to the Nessie catalog within the SQL client session. Run the following command first inside the Flink SQL client. This only needs to be done once per session.

```sql
CREATE CATALOG catalog WITH (
  'type' = 'iceberg',
  'catalog-impl' = 'org.apache.iceberg.nessie.NessieCatalog',
  'uri' = 'http://nessie:19120/api/v1',
  'ref' = 'main',
  'warehouse' = 'file:///tmp/warehouse'
);

-- After creating the catalog, tell Flink to use it.
USE CATALOG catalog;
```

#### **Creating a Table**
The `CREATE TABLE` syntax is nearly identical to Spark's, but does not use the `USING` clause.

```sql
CREATE TABLE ecommerce.customers (
    customer_id BIGINT,
    name STRING,
    email STRING,
    registration_date TIMESTAMP
)
-- It is good practice in Flink to specify the Iceberg format version
WITH ('format-version'='2') 
PARTITIONED BY (days(registration_date));
```

#### **Schema Evolution**
The `ALTER TABLE` commands are standard SQL and are identical to the Spark examples above.

---

## Training Questions

1.  Choose either the Spark SQL shell or the Flink SQL Client to perform these tasks.
2.  Create a new Iceberg table called `learning.movies` with columns for `movie_id` (INT), `title` (STRING), and `release_year` (INT).
3.  Add a new column called `genre` (STRING) to the `movies` table.
4.  Rename the `release_year` column to `year`.
5.  What are the benefits of using hidden partitioning in Iceberg?