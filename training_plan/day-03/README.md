# Day 3: Kafka Consumers

## Topic: Reading Messages from Kafka

Now that we know how to send messages to Kafka, let's learn how to read them. This is the job of a Kafka consumer.

A consumer subscribes to one or more topics and reads the messages in the order they were produced.

### Key Concepts

*   **Consumer Groups:** A consumer group is one or more consumers that work together to process the messages from a topic. The key rule is: **within a single consumer group, each partition is consumed by exactly one consumer.** This is how Kafka achieves scalable, parallel, and safe message processing.

    Let's imagine a topic with 4 partitions (P0, P1, P2, P3) and a group named `my-group`:

    *   **Scenario 1: 1 Consumer.** If you start one consumer in `my-group`, it will be assigned all 4 partitions.
        `Consumer 1  <-- reads from -->  [P0, P1, P2, P3]`

    *   **Scenario 2: 2 Consumers.** If you start a second consumer in `my-group`, Kafka automatically rebalances the load. 
        `Consumer 1  <-- reads from -->  [P0, P1]`
        `Consumer 2  <-- reads from -->  [P2, P3]`

    *   **Scenario 3: 4 Consumers.** If you start four consumers, each will be assigned one partition. This is the maximum level of parallelism for this topic.
        `Consumer 1 -> [P0]`, `Consumer 2 -> [P1]`, `Consumer 3 -> [P2]`, `Consumer 4 -> [P3]`

    *   **Scenario 4: 5 Consumers.** If you start a fifth consumer, it will be **idle**. It will not be assigned any partitions and will wait until another consumer leaves the group.

    This design is critical because it guarantees message ordering within a partition and prevents two consumers from processing the same message.
*   **Offset Management:** As a consumer reads messages, it needs to keep track of which messages it has already processed. This is done by committing the offset of the last processed message. Offsets are committed to a special Kafka topic called `__consumer_offsets`.
*   **Deserialization:** Just as producers serialize messages, consumers must deserialize them from a byte array back into an object or data structure that the application can use.

### Real-World Example

Continuing with our e-commerce example, let's say we have a `fraud_detection` service that needs to analyze the `orders` topic for potentially fraudulent orders.

We can have multiple instances of the `fraud_detection` service running in parallel, all part of the same consumer group (e.g., `fraud_detection_group`). If the `orders` topic has 4 partitions, we can run up to 4 instances of the `fraud_detection` service, and each instance will be assigned one partition to consume from. This allows us to process orders in parallel and scale our fraud detection system.

Here's a conceptual Python code snippet of how a consumer might read from the `orders` topic:

```python
from kafka import KafkaConsumer
import json

consumer = KafkaConsumer('orders',
                         bootstrap_servers=['localhost:9092'],
                         group_id='fraud_detection_group',
                         auto_offset_reset='earliest',
                         value_deserializer=lambda m: json.loads(m.decode('utf-8')))

for message in consumer:
    # message value and key are raw bytes -- decode if necessary!
    # e.g., for unicode: `message.value.decode('utf-8')`
    print(f"Received order: {message.value}")
    # Process the order here
```

## Training Questions

1.  Create a Python script (`consumer.py`) that reads messages from the `test_topic` you created yesterday. Print the value of each message to the console.
2.  Run your `producer.py` and `consumer.py` scripts at the same time. You should see the messages produced by the producer being consumed by the consumer.
3.  What is the purpose of a consumer group? What happens if you start multiple instances of your `consumer.py` script with the same `group_id`?
4.  What does the `auto_offset_reset` configuration do? What are the common values for this setting and what do they mean?
