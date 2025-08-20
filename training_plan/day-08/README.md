# Day 8: Querying Iceberg Tables

## Topic: Reading Data and Time Travel

Now that we have data in our Iceberg table, let's learn how to query it. Querying an Iceberg table is as simple as a standard `SELECT` statement. However, Iceberg has a killer feature that sets it apart: time travel.

---

### Option 1: Using Spark SQL

Connect to the Spark SQL shell with `docker exec -it spark-iceberg spark-sql`.

#### **Standard Queries**

You can query an Iceberg table using standard `SELECT` statements.

```sql
SELECT * FROM catalog.learning.movies WHERE year > 2000;

SELECT genre, count(*) AS count
FROM catalog.learning.movies
GROUP BY genre
ORDER BY count DESC;
```

#### **Time Travel**

Spark uses the `VERSION AS OF` and `TIMESTAMP AS OF` syntax to perform time-travel queries.

```sql
-- By Snapshot ID
SELECT * FROM catalog.learning.movies VERSION AS OF 1234567890123456789;

-- By Timestamp
SELECT * FROM catalog.learning.movies TIMESTAMP AS OF '2023-10-27 10:00:00';
```

---

### Option 2: Using Flink SQL

Connect to the Flink SQL Client with `docker exec -it flink sql-client` and be sure to [configure your catalog as described in Day 6](day-06/README.md).

#### **Standard Queries**

Standard `SELECT` statements work exactly the same in Flink.

```sql
SELECT * FROM learning.movies WHERE year > 2000;

SELECT genre, count(*) AS count
FROM learning.movies
GROUP BY genre
ORDER BY count DESC;
```

#### **Time Travel**

Flink uses a slightly different syntax for time travel, employing `FOR SYSTEM_TIME AS OF`.

```sql
-- By Timestamp
SELECT * FROM learning.movies
FOR SYSTEM_TIME AS OF TIMESTAMP '2023-10-27 10:00:00';

-- By Snapshot ID
SELECT * FROM learning.movies
FOR SYSTEM_TIME AS OF 'snapshot_id' -- e.g., '1234567890123456789'
```

---

### Real-World Example

Imagine you are a data scientist at a financial company. You have a table of stock prices that is updated every minute.

You are building a machine learning model to predict stock prices, and you want to train your model on the data as it was at the close of the market yesterday. With Iceberg, you can easily do this with a time-travel query.

Or, let's say you discover a bug in your data pipeline that caused some incorrect data to be written to the table. You can use time travel to see what the data looked like before the bug was introduced, and you can even roll back the table to that state.

## Training Questions

1.  Using either Spark or Flink, run a `SELECT *` query on your `movies` table to see all the data.
2.  Find the snapshot ID of the last commit you made to the `movies` table. You can find this in the table's history. (Hint: `SELECT * FROM catalog.learning.movies.history;`)
3.  Use the snapshot ID you found to run a time-travel query to see the state of the table at that snapshot.
4.  Update the genre of one of your movies. Now, run a time-travel query to see the state of the table *before* you made the update. You can use a timestamp for this.
5.  How could time travel be useful for debugging data quality issues?