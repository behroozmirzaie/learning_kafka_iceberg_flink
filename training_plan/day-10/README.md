# Day 10: Connecting Kafka and Spark Structured Streaming

## Topic: Introduction to Spark Structured Streaming

We've covered the basics of Kafka and Iceberg. Now, it's time to start connecting them. Today, we'll learn about Spark Structured Streaming, a scalable and fault-tolerant stream processing engine built on the Spark SQL engine.

### What is Structured Streaming?

Structured Streaming allows you to process streaming data in a way that is very similar to how you process batch data. You can express your streaming computation as a standard batch-like query on a DataFrame or Dataset.

The Spark SQL engine takes care of running it incrementally and continuously and updating the final result as streaming data continues to arrive.

### Reading from Kafka

Structured Streaming has a built-in connector for reading data from Kafka. You can create a streaming DataFrame that subscribes to a Kafka topic.

Here's a conceptual PySpark code snippet:

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

This code will:

1.  Create a SparkSession.
2.  Create a streaming DataFrame that reads from the `my_topic` Kafka topic.
3.  Select the key and value from the Kafka messages and cast them to strings.
4.  Write the resulting DataFrame to the console.

### Real-World Example

Imagine a social media company that wants to analyze the sentiment of tweets in real-time. They can have a Kafka topic where all new tweets are published.

They can then use Spark Structured Streaming to read the tweets from the Kafka topic, run a sentiment analysis model on each tweet, and then write the results to another Kafka topic or a database.

This allows them to have a real-time dashboard that shows the overall sentiment of tweets about their brand.

## Training Questions

1.  Create a new Python script (`stream_processor.py`).
2.  In this script, use PySpark to read from the `test_topic` you created on Day 2.
3.  Instead of writing to the console, try to parse the message if it's a JSON string and then select a specific field from the JSON.
4.  Run your `producer.py` script to send some JSON messages to `test_topic` and see if your `stream_processor.py` can correctly parse them.
5.  What is the difference between `outputMode("append")`, `outputMode("complete")`, and `outputMode("update")` in Structured Streaming?
