# Day 2: Kafka Producers

## Topic: Sending Messages to Kafka

Yesterday, we learned about the core concepts of Kafka. Today, we'll focus on producers and how they send messages to Kafka topics.

A producer is any application that writes data to Kafka. The producer's job is to connect to the Kafka cluster and send messages to the topic of its choice.

### Key Concepts

*   **Serialization:** Before a producer can send a message to Kafka, it must be serialized, meaning it needs to be converted into a byte array. Common serialization formats include JSON, Avro, and Protobuf. For simplicity, we'll start with plain strings.
*   **Message Keys:** When sending a message, you can optionally specify a key. The key is used by Kafka to determine which partition the message should be sent to. If the key is null, the producer will send the message to a random partition (round-robin). If a key is provided, all messages with the same key will go to the same partition. This is crucial for ordering.
*   **Acknowledgements (acks):** An acknowledgement is a confirmation signal (a "receipt") sent from a Kafka broker back to the producer to confirm that the message has been successfully received and saved. This setting is critical as it controls the **durability** of your messages—the guarantee that a sent message will not be lost. You can choose your desired level of durability, which creates a direct trade-off between safety and speed.

    *   `acks=0` (Fire and Forget): The producer sends the message and does not wait for any receipt.
        *   **Durability:** Lowest. Data can be lost if the broker is down or there's a network issue, and the producer will never know.
        *   **Analogy:** Dropping a letter in a public mailbox.

    *   `acks=1` (Leader Acknowledgement - Default): The producer waits for a receipt from the leader broker only.
        *   **Durability:** Good, but with a small window for data loss. If the leader crashes right after sending the receipt but before its followers have copied the message, the data is lost.
        *   **Analogy:** Getting a notification that your package was received by the local post office.

    *   `acks=all` (Full Acknowledgement): The producer waits for a receipt from the leader broker *and* all of its in-sync follower brokers.
        *   **Durability:** Highest. The message is confirmed to be replicated on multiple machines. If the leader crashes, another broker with a copy of the data will take over.
        *   **Analogy:** Sending a certified package that requires multiple signatures for proof of delivery.

### Important Producer Concepts

#### What is the `bootstrap_servers` argument?
This argument provides the initial contact point for the producer to connect to the Kafka cluster. The producer doesn't need to know the address of every broker. It connects to one of the bootstrap servers and receives a full list of all brokers in the cluster. From then on, it can connect to the correct brokers directly. It's like calling a company's main receptionist to get the direct line of the person you need to talk to.

#### Handling Non-Existent Topics
By default, a Kafka broker might be configured to automatically create a topic if a producer sends a message to one that doesn't exist (`auto.create.topics.enable=true`). This is dangerous in production. When this is disabled, the producer will not wait; it will fail. The `send()` command will raise an exception when you try to `flush()` or get the result of the future it returns. You should always handle this possibility in your code:

```python
# Example of handling a non-existent topic
from kafka.errors import KafkaTimeoutError

try:
    # This will fail if the topic doesn't exist and auto-creation is disabled
    producer.send('non-existent-topic', b'some value').get(timeout=10)
except KafkaTimeoutError:
    print("Failed to send: Topic does not exist or broker is unavailable.")
```

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