# Day 7: Writing Data to Iceberg Tables

## Topic: Inserting and Upserting Data

Now that we have a table, let's put some data in it. Iceberg supports several ways to write data, including standard `INSERT INTO` statements and the powerful `MERGE INTO` for upserts.

### Inserting Data

You can insert data into an Iceberg table using the standard `INSERT INTO` statement, just like with any other SQL table.

```sql
INSERT INTO catalog.learning.movies VALUES (1, 'The Shawshank Redemption', 1994, 'Drama');
INSERT INTO catalog.learning.movies VALUES (2, 'The Godfather', 1972, 'Crime');
```

### Upserting with `MERGE INTO`

One of the most powerful features of Iceberg is the ability to perform atomic upserts using the `MERGE INTO` statement. An upsert is an operation that will insert a new row if it doesn't exist, or update the existing row if it does.

This is extremely useful for many data warehousing and data lake use cases where you need to update existing records.

```sql
MERGE INTO catalog.learning.movies t
USING (SELECT 1 AS movie_id, 'The Shawshank Redemption' AS title, 1994 AS year, 'Crime/Drama' AS genre) s
ON t.movie_id = s.movie_id
WHEN MATCHED THEN
  UPDATE SET t.genre = s.genre
WHEN NOT MATCHED THEN
  INSERT (movie_id, title, year, genre) VALUES (s.movie_id, s.title, s.year, s.genre);
```

This statement will:

*   If a movie with `movie_id = 1` already exists, it will update its genre to `Crime/Drama`.
*   If a movie with `movie_id = 1` does not exist, it will insert a new row with the given data.

### Real-World Example

Consider a table that stores the current inventory of products in a warehouse. The table has columns for `product_id`, `location`, and `quantity`.

Every day, we get a new file with the latest inventory counts. This file contains a mix of new products and updated counts for existing products.

Instead of trying to figure out which products are new and which are updates, we can simply use a `MERGE INTO` statement to update the inventory table. We can load the new inventory data into a temporary table and then merge it into the main inventory table.

This makes the process of updating the inventory much simpler and more reliable.

## Training Questions

1.  Insert a few more of your favorite movies into the `catalog.learning.movies` table.
2.  The genre for "The Shawshank Redemption" is incorrect. It should be just "Drama". Write a `MERGE INTO` statement to correct the genre.
3.  You have a new movie to add: `movie_id: 3, title: 'The Dark Knight', year: 2008, genre: 'Action'`. Write a `MERGE INTO` statement to add this movie to the table.
4.  What is the difference between `INSERT OVERWRITE` and `MERGE INTO`? When would you use one over the other?
