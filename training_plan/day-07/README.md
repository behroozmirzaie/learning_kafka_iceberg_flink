# Day 7: Writing Data to Iceberg Tables

## Topic: Inserting and Upserting Data

Now that we have a table, let's put some data in it. Iceberg supports several ways to write data, including standard `INSERT INTO` statements and the powerful `MERGE INTO` for upserts.

---

### Option 1: Using Spark SQL

Connect to the Spark SQL shell with `docker exec -it spark-iceberg spark-sql`.

#### **Inserting Data**

You can insert data using a standard `INSERT INTO ... VALUES` statement.

```sql
INSERT INTO catalog.learning.movies VALUES (1, 'The Shawshank Redemption', 1994, 'Drama');
INSERT INTO catalog.learning.movies VALUES (2, 'The Godfather', 1972, 'Crime');
```

#### **Upserting with `MERGE INTO`**

Spark has excellent support for the `MERGE INTO` command, which allows you to atomically insert, update, or delete records. This is extremely useful for data warehousing use cases.

```sql
MERGE INTO catalog.learning.movies t
USING (SELECT 1 AS movie_id, 'The Shawshank Redemption' AS title, 1994 AS year, 'Crime/Drama' AS genre) s
ON t.movie_id = s.movie_id
WHEN MATCHED THEN
  UPDATE SET t.genre = s.genre
WHEN NOT MATCHED THEN
  INSERT (movie_id, title, year, genre) VALUES (s.movie_id, s.title, s.year, s.genre);
```

---

### Option 2: Using Flink SQL

Connect to the Flink SQL Client with `docker exec -it flink sql-client` and be sure to [configure your catalog as described in Day 6](day-06/README.md).

#### **Inserting Data**

Flink also supports the standard `INSERT INTO ... VALUES` syntax for simple, ad-hoc insertions.

```sql
INSERT INTO learning.movies VALUES (1, 'The Shawshank Redemption', 1994, 'Drama');
INSERT INTO learning.movies VALUES (2, 'The Godfather', 1972, 'Crime');
```

#### **Upserting with `MERGE INTO`**

Flink also supports `MERGE INTO`, allowing for powerful data update capabilities similar to Spark.

```sql
MERGE INTO learning.movies t
USING (SELECT 1 AS movie_id, 'The Shawshank Redemption' AS title, 1994 AS year, 'Crime/Drama' AS genre) s
ON t.movie_id = s.movie_id
WHEN MATCHED THEN
  UPDATE SET SET t.genre = s.genre
WHEN NOT MATCHED THEN
  INSERT (movie_id, title, year, genre) VALUES (s.movie_id, s.title, s.year, s.genre);
```
Note that Flink's primary strength is in streaming inserts, which we will cover in a later day.

---

### Real-World Example

Consider a table that stores the current inventory of products in a warehouse. The table has columns for `product_id`, `location`, and `quantity`.

Every day, we get a new file with the latest inventory counts. This file contains a mix of new products and updated counts for existing products.

Instead of trying to figure out which products are new and which are updates, we can simply use a `MERGE INTO` statement to update the inventory table. This makes the process of updating the inventory much simpler and more reliable.

## Training Questions

1.  Using either Spark or Flink, insert a few more of your favorite movies into the `learning.movies` table.
2.  The genre for "The Shawshank Redemption" is incorrect. It should be just "Drama". Write a `MERGE INTO` statement to correct the genre.
3.  You have a new movie to add: `movie_id: 3, title: 'The Dark Knight', year: 2008, genre: 'Action'`. Write a `MERGE INTO` statement to add this movie to the table.
4.  What is the difference between `INSERT OVERWRITE` and `MERGE INTO`? When would you use one over the other?