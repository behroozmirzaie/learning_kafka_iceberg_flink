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

### Exactly-Once Semantics and Fault Tolerance

Exactly-once semantics means that each message is processed exactly once, even in the presence of failures. This is crucial for applications where data loss or duplication is not acceptable, such as financial applications.

Achieving this requires a combination of a transactional source (like Kafka), a transactional sink (like Iceberg), and a stream processing engine that supports exactly-once processing (like Spark Structured Streaming).

#### What happens if the streaming application crashes?
This is a critical scenario. What if your Spark application reads a batch of data from Kafka, but crashes before it can write it to Iceberg? The system is designed to handle this without data loss.

1.  **Read from Kafka:** Spark reads a micro-batch from Kafka (e.g., offsets 1000-2000). The last *committed* offset in Kafka is still 1000.
2.  **The Crash:** The application crashes before writing to Iceberg.
3.  **Recovery:** Upon restart, the application first checks its checkpoint. The checkpoint tells it that it was working on offsets 1000-2000 but never finished.
4.  **Re-read:** The application asks Kafka for data starting from the last committed offset, which is still 1000. It re-reads the same batch of data and can attempt to write it to Iceberg again.

#### The Role of Checkpointing
The `checkpointLocation` you provide in your streaming query is the key to this fault tolerance. It's a directory on a reliable file system (like HDFS or S3) where the streaming engine saves its own internal state. This checkpoint acts as the application's "brain" and stores critical information for recovery, including:
*   The exact range of Kafka offsets the job was processing.
*   The status of the last transaction with the sink (Iceberg).
*   The state of any in-memory aggregations or calculations.

By combining Kafka's offsets, the engine's checkpoint, and the atomic commits of a transactional sink like Iceberg, you can build robust, end-to-end, exactly-once pipelines.

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