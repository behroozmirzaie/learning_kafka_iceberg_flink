# Day 16: Introduction to Apache Flink

## Topic: What is Apache Flink?

Welcome to your dedicated Flink training! While we've seen Flink as an engine for Iceberg, today we'll focus on what makes Flink a unique and powerful tool in its own right.

Flink is a distributed processing engine and a stateful stream processing framework. While Spark treats streaming as a series of fast batch jobs (micro-batching), Flink is a **true, event-at-a-time stream processor**. This architectural difference makes it exceptionally well-suited for use cases that require low latency, high throughput, and complex stateful computations.

### Core Flink Concepts

*   **True Streaming:** Flink processes events one by one as they arrive, allowing for millisecond-level processing latency.
*   **Stateful Stream Processing:** This is Flink's most important feature. Flink can maintain "state"—memory about past events—directly within the stream processor. This allows for sophisticated applications like fraud detection, anomaly detection, and machine learning on streams. We will cover this in depth tomorrow.
*   **Event Time vs. Processing Time:** Flink has first-class support for "event time," the time an event actually occurred at its source. This is critical for producing correct results even when data arrives late or out of order. This is in contrast to "processing time," which is simply the time the event was processed by the Flink operator.
*   **Multiple APIs:** Flink provides several layers of APIs:
    *   **DataStream API (Java/Scala/Python):** A powerful, low-level API for writing complex stream processing logic.
    *   **Table API & SQL:** A high-level, declarative API for querying and processing streams as if they were tables. This is what we have been using so far.

### Flink vs. Spark

| Feature | Apache Flink | Apache Spark |
| :--- | :--- | :--- |
| **Core Model** | True Stream Processor (event-at-a-time) | Batch Processor (micro-batch streaming) |
| **State Management** | First-class, robust operator state | Good, but less flexible (DStreams/Structured) |
| **Latency** | Milliseconds | Seconds to sub-seconds |
| **Use Case** | Complex event processing, low-latency alerting | ETL, batch processing, interactive analytics |

### Real-World Examples

*   **Financial Services:** A bank uses Flink to power a real-time fraud detection system. For every credit card transaction, a Flink job can look up the user's recent transaction history (stored in Flink's state) to spot anomalous patterns and block fraudulent transactions within milliseconds.
*   **E-commerce:** An online retailer uses Flink to generate real-time product recommendations. As a user clicks on items, a Flink job updates its recommendations instantly based on the user's activity and similar users' behavior.
*   **Telecommunications:** A mobile carrier uses Flink to monitor its network for dropped calls or service degradation, allowing them to detect and respond to outages in real-time.

## Training Questions

1.  In your own words, what is the main architectural difference between how Flink and Spark handle streaming data?
2.  Why is "stateful" processing so important for streaming applications? Give an example.
3.  What is the difference between "event time" and "processing time"? Why would you choose one over the other?
4.  For the e-commerce recommendation example, what kind of information would the Flink job need to keep in its state?
