# Day 14: Advanced Iceberg Concepts

## Topic: Snapshots, Metadata, and Other Integrations

We've covered the basics of using Iceberg with Spark. Today, we'll explore some of the more advanced concepts that make Iceberg so powerful and flexible.

### Understanding Snapshots

As we know, every write to an Iceberg table creates a new snapshot. A snapshot is a complete list of all the data files that make up the table at a specific point in time.

Iceberg's metadata is hierarchical:

1.  **Table Metadata File:** This is the root of the table's metadata. It contains information about the table's schema, partitioning, and a pointer to the current snapshot.
2.  **Manifest List:** Each snapshot has a manifest list, which is a list of all the manifest files that make up that snapshot.
3.  **Manifest File:** A manifest file contains a list of data files, along with statistics about the data in each file (e.g., min/max values for each column).

This hierarchical structure allows Iceberg to perform fast metadata operations and efficient query planning.

### Other Integrations

While we've been using Spark to interact with Iceberg, one of the great things about Iceberg is that it's an open standard. This means that many different processing engines can read from and write to Iceberg tables.

Some of the other popular engines that support Iceberg include:

*   **Trino (formerly PrestoSQL):** A fast, distributed SQL query engine.
*   **Flink:** A powerful stream processing engine.
*   **Hive:** You can even use Hive to read from and write to Iceberg tables.

This interoperability means that you are not locked into a single processing engine. You can use the best tool for the job.

### Real-World Example

Imagine a large e-commerce company that has a central data lake built on Iceberg.

*   The data engineering team uses Spark to build ETL pipelines that ingest data from various sources and write it to Iceberg tables.
*   The data science team uses Trino to run ad-hoc analytical queries on the Iceberg tables.
*   The machine learning team uses Flink to build real-time recommendation engines that read from the Iceberg tables.

This is a great example of how Iceberg can serve as a central, reliable source of truth for a variety of different use cases and processing engines.

## Training Questions

1.  How does Iceberg's metadata structure help with query planning?
2.  What are the benefits of having a table format that is independent of the processing engine?
3.  Explore the files that make up your `movies` table in the `/tmp/warehouse` directory inside the `spark-iceberg` container. Can you find the metadata file, manifest lists, and manifest files?
4.  What is the role of the Nessie catalog in our setup?
5.  Research another tool that integrates with Iceberg (e.g., Dremio, Starburst). How does it use Iceberg?
