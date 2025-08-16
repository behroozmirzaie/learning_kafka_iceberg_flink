# Day 6: Creating Iceberg Tables

## Topic: Defining Your First Iceberg Table

Now that we have a conceptual understanding of Iceberg, let's get our hands dirty and create our first Iceberg table. We will use Spark SQL, which is one of the most common ways to interact with Iceberg.

Our `docker-compose.yml` file has already set up a Spark container with Iceberg pre-configured. We can use `spark-sql` to execute SQL commands.

### Creating a Table

The syntax for creating an Iceberg table is similar to creating a standard SQL table, with a few additions for Iceberg-specific features.

Here's a basic example:

```sql
CREATE TABLE catalog.db.my_table (
    id BIGINT,
    data STRING
)
USING iceberg;
```

Let's break this down:

*   `catalog.db.my_table`: This is the three-level namespace for Iceberg tables. `catalog` is the name of the catalog we configured in our Spark setup (in our case, it's just `catalog`), `db` is the database name, and `my_table` is the table name.
*   `USING iceberg`: This tells Spark to create an Iceberg table.

### Schema Evolution

One of the key features of Iceberg is its powerful schema evolution. You can easily add, drop, rename, and reorder columns.

```sql
-- Add a new column
ALTER TABLE catalog.db.my_table ADD COLUMN new_col STRING;

-- Rename a column
ALTER TABLE catalog.db.my_table RENAME COLUMN data TO payload;

-- Drop a column
ALTER TABLE catalog.db.my_table DROP COLUMN new_col;
```

These operations only change the table's metadata and don't require rewriting the data files, making them very fast.

### Real-World Example

Let's create a table to store customer data for our e-commerce site. The table will have columns for customer ID, name, email, and registration date.

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

Here, we've also added a `PARTITIONED BY` clause. We are partitioning the data by the day of the registration date. This is an example of hidden partitioning. We don't need to create a separate `registration_day` column; Iceberg will automatically extract the day from the `registration_date` timestamp.

## Training Questions

1.  Attach to the `spark-iceberg` container using `docker exec -it spark-iceberg spark-sql`. You should see a `spark-sql>` prompt.
2.  Create a new Iceberg table called `catalog.learning.movies` with columns for `movie_id` (INT), `title` (STRING), and `release_year` (INT).
3.  Add a new column called `genre` (STRING) to the `movies` table.
4.  Rename the `release_year` column to `year`.
5.  What are the benefits of using hidden partitioning in Iceberg?
