# Day 9: Iceberg Partitioning and Maintenance

## Topic: Efficient Data Layout and Optimization

Today, we'll explore two important aspects of managing large Iceberg tables: partitioning and maintenance.

### Partitioning

Partitioning is the key to achieving good performance with large tables. It divides a table into smaller parts based on column values, allowing for **partition pruning** where query engines skip reading irrelevant data.

Iceberg's **hidden partitioning** is a key feature. It creates partition values from source columns using transform functions (`days`, `hours`, `bucket`, `truncate`) without creating extra physical columns in your table. This saves space and prevents user error.

### Choosing a Good Partitioning Strategy

Choosing the right partitioning strategy is critical for performance. The goal is to create partitions that are not too big and not too small.

*   **Bad:** Partitioning by a high-cardinality column like `customer_id` creates millions of tiny partitions and excessive metadata, which is very slow.
*   **Bad:** Partitioning by a low-cardinality but skewed column like `country` (where one country has 90% of the data) creates a few huge partitions, which reduces the effectiveness of pruning.
*   **Good:** Partitioning by a time-based column (`day(event_ts)`) is often a great start. For high-cardinality columns like `user_id`, combining transforms is powerful: `PARTITIONED BY (days(event_ts), bucket(512, user_id))`.

As a rule of thumb, aim for partitions that are several hundred megabytes (MB) to a few gigabytes (GB) in size.

---

### Table Maintenance

Over time, write operations can create many small data files, which hurts read performance. Iceberg provides maintenance procedures to compact these small files into larger, more optimal ones.

#### **Using Spark SQL**
Spark provides a rich set of `CALL` procedures for maintenance.

```sql
-- Compact the entire table
CALL catalog.system.rewrite_data_files(table => 'db.my_table');

-- Compact a specific partition using a WHERE clause
CALL catalog.system.rewrite_data_files(
    table => 'db.my_table',
    where => 'event_ts >= \'2023-10-27 00:00:00\' AND event_ts < \'2023-10-28 00:00:00\'
);

-- Expire old snapshots to clean up metadata and data files
CALL catalog.system.expire_snapshots(table => 'db.my_table', older_than => TIMESTAMP '2023-10-01 00:00:00');
```

#### **Using Flink SQL**
Flink also provides table procedures for maintenance, though the syntax and available options may differ slightly from Spark.

```sql
-- Flink's compaction procedure is executed via an INSERT statement
-- This command compacts the whole table
INSERT INTO my_table_compacted /*+ OPTIONS('compaction.small-file.size'='1048576') */
SELECT * FROM my_table;

-- To compact specific partitions, you can add a WHERE clause to the SELECT statement.

-- Flink does not have a direct equivalent to expire_snapshots via SQL.
-- Snapshot expiration is typically handled via the Flink DataStream or Java APIs,
-- or by using the Spark procedure on the same table.
```

---

## Training Questions

1.  Create a new table called `catalog.learning.sales` with columns for `sale_id` (INT), `product` (STRING), `amount` (DOUBLE), and `sale_ts` (TIMESTAMP). Partition the table by `month(sale_ts)`.
2.  Insert some sample sales data into the table for different months.
3.  Run a query to select the sales for a specific month. How does Iceberg use partitioning to make this query efficient?
4.  Using Spark, run the `rewrite_data_files` procedure to compact your `sales` table.
5.  What are the benefits of regularly compacting an Iceberg table?
