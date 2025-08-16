# Day 12: Kafka to Iceberg Pipeline (Part 2)

## Topic: Handling Late Data and Exactly-Once Semantics

Yesterday, we built a simple streaming pipeline. Today, we'll look at two important real-world challenges in stream processing: handling late-arriving data and ensuring exactly-once processing semantics.

### Handling Late-Arriving Data

In a distributed system, it's common for events to arrive out of order or later than expected. For example, a truck might lose its network connection and then send a batch of old location updates when it comes back online.

Structured Streaming provides a mechanism called **watermarking** to handle late-arriving data. A watermark is a threshold that tells the streaming engine how long to wait for late data. Any data that arrives after the watermark has passed is considered too late and is dropped.

```python
# ... (read from Kafka and parse)

# Define a watermark on the event_time column
with_watermark_df = parsed_df.withWatermark("event_time", "10 minutes")

# ... (perform aggregations)
```

### Exactly-Once Semantics

Exactly-once semantics means that each message is processed exactly once, even in the presence of failures. This is crucial for applications where data loss or duplication is not acceptable, such as financial applications.

Achieving exactly-once semantics requires a combination of a transactional source (like Kafka), a transactional sink (like Iceberg), and a stream processing engine that supports exactly-once processing (like Spark Structured Streaming).

Iceberg's atomic commits and Spark's checkpointing mechanism work together to provide exactly-once semantics when writing to an Iceberg table. As long as your source is also transactional (Kafka is), you can achieve end-to-end exactly-once guarantees.

### Real-World Example

Consider an online gaming platform that tracks player scores in real-time. The scores are sent to a Kafka topic.

The platform wants to calculate the leaderboard for a tournament. It's important that each player's score is counted exactly once, even if there are network issues or server failures.

By using a Kafka-to-Iceberg pipeline with Spark Structured Streaming, the platform can ensure that the leaderboard is always accurate and consistent.

They can also use watermarking to handle cases where a player's device might be temporarily disconnected from the internet.

## Training Questions

1.  What is the problem with late-arriving data in stream processing?
2.  How does watermarking in Spark Structured Streaming help to solve this problem?
3.  Modify your streaming script from yesterday to include a watermark on the `timestamp` column.
4.  What are the three components needed to achieve end-to-end exactly-once semantics?
5.  Why is exactly-once processing important for an application that processes financial transactions?
