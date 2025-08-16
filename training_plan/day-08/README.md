# Day 8: Querying Iceberg Tables

## Topic: Reading Data and Time Travel

Now that we have data in our Iceberg table, let's learn how to query it. Querying an Iceberg table is as simple as a standard `SELECT` statement. However, Iceberg has a killer feature that sets it apart: time travel.

### Standard Queries

You can query an Iceberg table using the same `SELECT` statements you are used to.

```sql
SELECT * FROM catalog.learning.movies WHERE year > 2000;

SELECT genre, count(*) AS count
FROM catalog.learning.movies
GROUP BY genre
ORDER BY count DESC;
```

### Time Travel

Every write operation to an Iceberg table creates a new snapshot. A snapshot represents the state of the table at a specific point in time. Iceberg allows you to query any snapshot of the table, which is like traveling back in time to see what the data looked like at that moment.

You can time travel in two ways:

1.  **By Snapshot ID:** You can query a specific snapshot by its ID.

    ```sql
    SELECT * FROM catalog.learning.movies VERSION AS OF 1234567890123456789;
    ```

2.  **By Timestamp:** You can query the state of the table as it was at a specific timestamp.

    ```sql
    SELECT * FROM catalog.learning.movies TIMESTAMP AS OF '2023-10-27 10:00:00';
    ```

### Real-World Example

Imagine you are a data scientist at a financial company. You have a table of stock prices that is updated every minute.

You are building a machine learning model to predict stock prices, and you want to train your model on the data as it was at the close of the market yesterday. With Iceberg, you can easily do this with a time-travel query.

Or, let's say you discover a bug in your data pipeline that caused some incorrect data to be written to the table. You can use time travel to see what the data looked like before the bug was introduced, and you can even roll back the table to that state.

## Training Questions

1.  Run a `SELECT *` query on your `movies` table to see all the data.
2.  Find the snapshot ID of the last commit you made to the `movies` table. You can find this in the table's history. (Hint: `SELECT * FROM catalog.learning.movies.history;`)
3.  Use the snapshot ID you found to run a time-travel query to see the state of the table at that snapshot.
4.  Update the genre of one of your movies. Now, run a time-travel query to see the state of the table *before* you made the update. You can use a timestamp for this.
5.  How could time travel be useful for debugging data quality issues?
