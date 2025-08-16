# Day 2: Kafka Producers

## Topic: Sending Messages to Kafka

Yesterday, we learned about the core concepts of Kafka. Today, we'll focus on producers and how they send messages to Kafka topics.

A producer is any application that writes data to Kafka. The producer's job is to connect to the Kafka cluster and send messages to the topic of its choice.

### Key Concepts

*   **Serialization:** Before a producer can send a message to Kafka, it must be serialized, meaning it needs to be converted into a byte array. Common serialization formats include JSON, Avro, and Protobuf. For simplicity, we'll start with plain strings.
*   **Message Keys:** When sending a message, you can optionally specify a key. The key is used by Kafka to determine which partition the message should be sent to. If the key is null, the producer will send the message to a random partition (round-robin). If a key is provided, all messages with the same key will go to the same partition. This is crucial for ordering.
*   **Acknowledgements (acks):** When a producer sends a message, it can configure the level of acknowledgement it requires from the broker. This controls the durability of the message.
    *   `acks=0`: The producer doesn't wait for any acknowledgement. This is the fastest but least durable option (fire-and-forget).
    *   `acks=1`: The producer waits for the leader broker to acknowledge the message. This is the default.
    *   `acks=all`: The producer waits for the leader and all in-sync replicas to acknowledge the message. This is the most durable option.

### Real-World Example

Let's go back to our e-commerce website. The `product_reviews` service needs to send review events to a `reviews` topic. Each review has a `product_id`.

To ensure that all reviews for the same product are processed in the order they were submitted, the producer should use the `product_id` as the message key. This guarantees that all reviews for a given product will end up in the same partition, and thus be consumed in order.

Here's a conceptual Python code snippet of how a producer might send a review:

```python
from kafka import KafkaProducer
import json

producer = KafkaProducer(bootstrap_servers='localhost:9092',
                         value_serializer=lambda v: json.dumps(v).encode('utf-8'))

review = {
    'product_id': 'p123',
    'user_id': 'u456',
    'rating': 5,
    'comment': 'This is a great product!'
}

# Use product_id as the key to ensure ordering
producer.send('reviews', key=b'p123', value=review)
producer.flush() # Block until all async messages are sent
```

## Training Questions

1.  Create a simple Python script (`producer.py`) that sends a message to a Kafka topic named `test_topic`. You can use the `kafka-python` library. Your message can be a simple string like "Hello, Kafka!".
2.  Modify your `producer.py` script to send 10 messages in a loop. For each message, use a key. For example, you can use `key-1`, `key-2`, etc.
3.  What would happen if you sent messages with the same key to a topic with multiple partitions? Why is this useful?
4.  Explain the trade-offs between `acks=0`, `acks=1`, and `acks=all` in terms of performance and durability.
