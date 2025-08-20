# Day 10: Connecting Kafka to Streaming Engines

We've covered the basics of Kafka and Iceberg. Now, it's time to start connecting them with a stream processing engine. This allows you to read data continuously from Kafka and process it in near-real-time.

---

### Option 1: Using Spark Structured Streaming

Spark Structured Streaming is a stream processing engine built on the Spark SQL engine. It lets you work with streaming data using the same DataFrame API you would use for batch data.

#### **Reading from Kafka with PySpark**

The most common way to use Structured Streaming is with the DataFrame API in Python or Scala. Here's a conceptual PySpark code snippet:

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("KafkaToConsole").getOrCreate()

# Create a streaming DataFrame that reads from a Kafka topic
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "my_topic") \
    .load()

# The data from Kafka is in a binary format, so we need to cast it to a string
query = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)") \
    .writeStream \
    .outputMode("append") \
    .format("console") \
    .start()

query.awaitTermination()
```
This code defines a streaming query that reads from Kafka and prints the results to the console. The query runs continuously, processing data as it arrives.

---

### Option 2: Using Flink SQL

Flink allows you to treat a Kafka topic as a SQL table. You can declare a table using `CREATE TABLE` and then run continuous queries against it.

First, connect to the Flink SQL Client: `docker exec -it flink sql-client`

#### **Declaring a Kafka Table**

You can define a table that is backed by a Kafka topic. Flink will handle the connection and reading of messages.

```sql
CREATE TABLE kafka_source_table (
  `user_id` STRING,
  `event_timestamp` TIMESTAMP(3),
  `url` STRING
  -- Use METADATA columns to access Kafka-specific fields
  -- `key` METADATA, 
  -- `topic` METADATA,
  -- `partition` METADATA,
  -- `offset` METADATA
) WITH (
  'connector' = 'kafka',
  'topic' = 'user_activity', -- The Kafka topic to read from
  'properties.bootstrap.servers' = 'kafka:9092',
  'scan.startup.mode' = 'earliest-offset',
  'value.format' = 'json' -- Specify the format of the message value
);
```

#### **Running a Continuous Query**

Once the table is declared, you can run a `SELECT` query on it. Unlike a normal database query, this query will **not** terminate. It will run forever, continuously processing the new messages that arrive in the Kafka topic and emitting updated results.

```sql
-- This is a continuous query that will never end
SELECT * FROM kafka_source_table;
```

---

## Training Questions

1.  Choose either Spark or Flink for this exercise.
2.  Create a new Kafka topic called `user_activity`.
3.  Write a producer that sends JSON messages to this topic. Each message should contain fields like `user_id`, `event_timestamp`, and `url`.
4.  Write a streaming application (either PySpark or Flink SQL) that reads from the `user_activity` topic and prints the parsed data to the console.
5.  What is the difference between `outputMode("append")` in Spark and a continuous query in Flink? How are they similar?

```