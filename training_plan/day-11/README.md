# Day 11: Kafka to Iceberg Pipeline (Part 1)

## Topic: Building a Simple Streaming Pipeline

Today is an exciting day! We're going to combine everything we've learned so far and build our first end-to-end streaming pipeline that reads data from Kafka and writes it to an Iceberg table.

### The Goal

Our goal is to create a continuous pipeline that:

1.  Reads messages from a Kafka topic.
2.  Parses and transforms the messages.
3.  Writes the transformed data to an Iceberg table.

### The Tools

*   **Kafka:** As our streaming source.
*   **Spark Structured Streaming:** As our processing engine.
*   **Iceberg:** As our destination table format.

### The Code

Here's a conceptual PySpark code snippet that demonstrates how to write a streaming DataFrame to an Iceberg table:

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
kafka_df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "my_data_topic") \
    .load()

# Parse the JSON data
parsed_df = kafka_df \
    .select(from_json(col("value").cast("string"), schema).alias("data")) \
    .select("data.*")

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

### Real-World Example

Consider a fleet management company that tracks the location of its trucks in real-time. The trucks send their location data to a Kafka topic.

The company wants to store this location data in a data lake for historical analysis. They can use a Spark Structured Streaming job to read the location data from Kafka, enrich it with other data (e.g., driver information), and then write it to an Iceberg table partitioned by date and truck ID.

This allows them to have a near real-time view of their fleet's location, as well as a historical record of all truck movements.

## Training Questions

1.  Create a new Kafka topic called `sensor_data`.
2.  Create a new Iceberg table called `catalog.learning.sensor_readings` with columns for `sensor_id` (STRING), `timestamp` (TIMESTAMP), and `reading` (DOUBLE). Partition the table by `sensor_id`.
3.  Create a producer script that sends JSON messages to the `sensor_data` topic. Each message should contain a `sensor_id`, a `timestamp`, and a `reading`.
4.  Create a Spark Structured Streaming script that reads from the `sensor_data` topic, parses the JSON, and writes the data to the `sensor_readings` Iceberg table.
5.  Run your producer and streaming scripts. After a few minutes, query the `sensor_readings` table to see if the data has been written correctly.

