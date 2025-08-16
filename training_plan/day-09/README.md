# Day 9: Iceberg Partitioning and Maintenance

## Topic: Efficient Data Layout and Optimization

Today, we'll explore two important aspects of managing large Iceberg tables: partitioning and maintenance.

### Partitioning

Partitioning is the key to achieving good performance with large tables. Partitioning divides a table into smaller, more manageable parts based on the values of one or more columns.

When you query a partitioned table with a filter on the partition column, Iceberg can skip reading the partitions that don't match the filter. This is called partition pruning and can dramatically improve query performance.

As we saw on Day 6, Iceberg supports **hidden partitioning**. This means you can partition by a transformed value of a column, without having to create a separate column for the transformed value.

For example, you can partition a table of events by the hour of the event timestamp:

```sql
CREATE TABLE catalog.logs.events (
    event_id BIGINT,
    event_ts TIMESTAMP,
    message STRING
)
USING iceberg
PARTITIONED BY (hours(event_ts));
```

When you query this table with a filter on `event_ts`, Iceberg will automatically prune the partitions that are not relevant to the query.

### Maintenance

Over time, as you write data to an Iceberg table, it can become fragmented with many small data files. This can hurt query performance. Iceberg provides maintenance operations to optimize the layout of the data.

*   **Compaction:** Compaction is the process of rewriting small data files into larger, more optimal files. This can significantly improve read performance.

    ```sql
    -- Compact the entire table
    CALL catalog.system.rewrite_data_files('db.my_table');

    -- Compact a specific partition
    CALL catalog.system.rewrite_data_files(table => 'db.my_table', where => 'event_ts >= \'2023-10-27 00:00:00\' AND event_ts < \'2023-10-28 00:00:00\'');
    ```

*   **Snapshot Expiration:** Every write to an Iceberg table creates a new snapshot. Over time, you can accumulate a large number of snapshots. You can expire old snapshots to clean up the table's history and delete the data files that are no longer needed.

### Real-World Example

Consider a table that stores website clickstream data. This table can grow to be very large, with billions of events per day.

To manage this table effectively, we can partition it by the hour of the event timestamp. This will allow us to efficiently query the data for a specific time range.

We can also set up a daily job that compacts the data for the previous day. This will ensure that the table is always optimized for read performance.

Finally, we can set up a policy to expire snapshots that are older than 30 days. This will keep the table's history manageable and reclaim storage space.

## Training Questions

1.  Create a new table called `catalog.learning.sales` with columns for `sale_id` (INT), `product` (STRING), `amount` (DOUBLE), and `sale_ts` (TIMESTAMP). Partition the table by month of the `sale_ts`.
2.  Insert some sample sales data into the table for different months.
3.  Run a query to select the sales for a specific month. How does Iceberg use partitioning to make this query efficient?
4.  Run the `rewrite_data_files` procedure to compact your `sales` table.
5.  What are the benefits of regularly compacting an Iceberg table?

```