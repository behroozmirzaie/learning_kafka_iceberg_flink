# Day 5: Introduction to Apache Iceberg

## Topic: What is Apache Iceberg?

Welcome to the second part of our training! Today, we shift our focus to Apache Iceberg, a modern table format for huge analytic datasets.

### Why Do We Need a New Table Format?

Traditional data lakes often use table formats like Hive. While Hive has been a workhorse for a long time, it has some limitations, especially in the age of cloud data lakes:

*   **Schema Evolution is Difficult:** Changing the schema of a large Hive table can be a slow and error-prone process.
*   **Partitioning is Inflexible:** Hive's partitioning scheme is based on directory structures, which can lead to performance issues with a large number of partitions.
*   **Lack of ACID Transactions:** Hive tables don't support ACID (Atomicity, Consistency, Isolation, Durability) transactions, making it hard to reliably update data.

### What is Iceberg?

Iceberg is an open table format designed to address these limitations. It's not a storage engine or a processing engine; it's a specification for how to manage a large, slow-moving dataset in a distributed file system like HDFS or a cloud object store like Amazon S3.

#### Iceberg is a Format, Not a Service

This is the most important concept to understand. You cannot "run" Iceberg by itself. It is a set of rules—a blueprint—for how data files and metadata files should be organized. To actually read or write data in the Iceberg format, you need a **computation engine** like Apache Spark or Apache Flink. In our setup, the `spark-iceberg` and `flink` containers are the engines that understand the Iceberg blueprint and do the actual work.

#### The Two Meanings of "Iceberg": Format vs. API

The name "Iceberg" can be confusing because it refers to two related but distinct concepts:

1.  **The Table Format (The Blueprint for Data):** This is the physical specification for how data is stored. It defines the structure of metadata files, manifest files, and data files on disk. This is the core of Iceberg.

2.  **The REST API (The Blueprint for Communication):** This is a standardized protocol that defines how engines (like Spark) should communicate with a **catalog** (like Nessie). The API provides a standard way to request information like "Create a table," "What is the current state of this table?," or "Commit these changes." Nessie implements this API, acting like a librarian that an engine can talk to. The engine asks the librarian (Nessie) where to find a book (a table's metadata), and the librarian points to the right location. The engine then uses its knowledge of the Table Format to actually read the book.

### Core Concepts

*   **Tables:** An Iceberg table is a collection of data files, just like a Hive table. However, the state of an Iceberg table is managed by a metadata file, not by a directory listing.
*   **Snapshots:** Every change to an Iceberg table creates a new snapshot. A snapshot represents the state of the table at a specific point in time. This enables features like time-travel queries and atomic updates.
*   **Schema Evolution:** Iceberg has a rich schema evolution model that allows you to safely add, drop, rename, and reorder columns.
*   **Hidden Partitioning:** Iceberg can automatically partition data based on the values in a column, without you having to create a complex directory structure. This makes partitioning much more flexible and efficient.

### Real-World Example

Imagine a large retail company that stores its sales data in a data lake. The sales data is constantly being updated with new orders, returns, and price changes.

With a traditional Hive table, updating this data would be a complex and slow process. With Iceberg, the company can use `MERGE INTO` statements to atomically update the sales data. They can also use time-travel queries to see what the sales data looked like at the end of last quarter, without having to restore a backup.

Furthermore, they can evolve the schema of the sales table over time, for example, by adding a new column for customer loyalty status, without having to rewrite the entire table.

## Training Questions

1.  In your own words, what are the main advantages of Iceberg over traditional Hive tables?
2.  What is a snapshot in Iceberg, and why is it useful?
3.  How does hidden partitioning in Iceberg work, and why is it better than Hive's partitioning scheme?
4.  Think of a dataset you are familiar with. How would you model it as an Iceberg table? What would be the schema and partitioning strategy?