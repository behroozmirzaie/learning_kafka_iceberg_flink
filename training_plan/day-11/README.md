# Day 11: Kafka to Iceberg Pipeline (Part 1)

## Topic: Building a Simple Streaming Pipeline

Today is an exciting day! We're going to combine everything we've learned so far and build our first end-to-end streaming pipeline that reads data from Kafka and writes it to an Iceberg table.

### The Goal

Our goal is to create a continuous pipeline that reads messages from a Kafka topic, parses and transforms them, and writes the data to an Iceberg table. This is a cornerstone of the modern data stack.

---

### Option 1: Using Spark Structured Streaming

This approach uses a PySpark script to define the streaming pipeline.

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import from_json, col
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

spark = SparkSession.builder.appName("KafkaToIceberg").getOrCreate()

# Define the schema for the incoming JSON data
schema = StructType([
    StructField("id", IntegerType(), True),
    StructField("data", StringType(), True)
])

# Read from Kafka
kafka_df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092").option("subscribe", "my_data_topic").load()

# Parse the JSON data
parsed_df = kafka_df.select(from_json(col("value").cast("string"), schema).alias("data")).select("data.*")

# Write to Iceberg
query = parsed_df.writeStream \
    .format("iceberg") \
    .outputMode("append") \
    .trigger(processingTime='1 minute') \
    .option("path", "catalog.db.my_iceberg_table") \
    .option("checkpointLocation", "/tmp/checkpoints") \
    .start()

query.awaitTermination()
```

---

### Option 2: Using Flink SQL

This approach uses Flink's powerful SQL client to define the entire pipeline declaratively.

First, connect to the Flink SQL Client: `docker exec -it flink sql-client`

**Step 1: Define the Source Table (Kafka)**
First, we declare a table that maps to our source Kafka topic, just like we did in Day 10.
```sql
CREATE TABLE kafka_source (
  `id` INT,
  `data` STRING
) WITH (
  'connector' = 'kafka',
  'topic' = 'my_data_topic',
  'properties.bootstrap.servers' = 'kafka:9092',
  'scan.startup.mode' = 'earliest-offset',
  'value.format' = 'json'
);
```

**Step 2: Define the Sink Table (Iceberg)**
Next, we declare our target Iceberg table. The schema must match the data we want to insert.
```sql
-- Make sure you have configured the Nessie catalog first!
USE CATALOG catalog;

CREATE TABLE my_iceberg_table (
  `id` INT,
  `data` STRING
);
```

**Step 3: Run the `INSERT` Job**
Finally, we start the continuous streaming job with a single `INSERT` statement. Flink will continuously read from `kafka_source` and write the results into `my_iceberg_table`.
```sql
-- This is a long-running streaming job, not a one-time command
INSERT INTO my_iceberg_table SELECT id, data FROM kafka_source;
```

---

### The Importance of Micro-Batching and Triggers

A key challenge when writing a high-velocity stream to a file-based format like Iceberg is avoiding the creation of millions of tiny files and snapshots. This is solved by **micro-batching**. The streaming engine collects messages in memory and writes the entire "micro-batch" in a single transaction.

*   In **Spark**, this is configured with the `.trigger()` command (e.g., `trigger(processingTime='1 minute')`).
*   In **Flink**, this is configured with sink properties like `sink.rolling-policy.rollover-interval`.

This provides a balance between near-real-time data ingestion and the need to write data efficiently to the data lake.

## Training Questions

1.  Create a new Kafka topic called `sensor_data`.
2.  Using either Spark or Flink, create a new Iceberg table called `catalog.learning.sensor_readings` with columns for `sensor_id` (STRING), `timestamp` (TIMESTAMP), and `reading` (DOUBLE). Partition the table by `sensor_id`.
3.  Create a producer script that sends JSON messages to the `sensor_data` topic.
4.  Create a streaming application (PySpark or Flink SQL) that reads from the `sensor_data` topic, parses the JSON, and writes the data to the `sensor_readings` Iceberg table.
5.  Run your producer and streaming scripts. After a few minutes, query the `sensor_readings` table to see if the data has been written correctly.
