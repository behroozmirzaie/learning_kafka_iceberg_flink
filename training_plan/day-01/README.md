# Day 1: Introduction to Kafka

## Topic: What is Apache Kafka?

Welcome to your first day of learning Kafka! Today, we'll cover the fundamental concepts of Apache Kafka and why it has become a cornerstone of modern data architectures.

Kafka is a distributed event streaming platform. But what does that mean in simple terms? Imagine a central nervous system for your company's data. Applications can send data (events) to this central system, and other applications can listen and react to that data in real-time.

### Core Concepts

*   **Events (or Messages):** An event is a small piece of data that represents something that happened. For example, a user clicking a button on a website, a new order being placed, or a sensor reading from an IoT device. In Kafka, events are stored as key-value pairs.
*   **Producers:** Producers are applications that create and send events to Kafka.
*   **Consumers:** Consumers are applications that read and process events from Kafka.
*   **Topics:** Topics are like folders for events. Events are organized and stored in topics. For example, you might have a `user_clicks` topic and an `orders` topic.
*   **Partitions:** Topics are split into partitions. Each partition is an ordered, immutable sequence of events. Partitions allow you to parallelize a topic by splitting the data over multiple brokers.
*   **Offsets:** Each event within a partition has a unique ID called an offset. Consumers keep track of which events they have processed by storing the offset.
*   **Brokers:** A Kafka cluster is made up of one or more servers called brokers. Brokers are responsible for storing data and serving client requests.

### Real-World Example

Imagine an e-commerce website. When a user places an order, the `orders` service (a producer) sends an `order_placed` event to a Kafka topic called `orders`.

This event could contain information like:
*   **Key:** `user_id`
*   **Value:** `{ "order_id": "123", "product": "Laptop", "amount": 1500 }`

Now, several other services (consumers) can subscribe to the `orders` topic and react to this event in real-time:
*   The `inventory` service can decrement the stock of the purchased product.
*   The `shipping` service can start the shipping process.
*   The `notifications` service can send an email confirmation to the user.

This is a simple example of how Kafka can be used to decouple services and enable real-time data processing.

## Training Questions

1.  In your own words, what is the difference between a Kafka topic and a partition?
2.  Think of another real-world scenario where Kafka could be used. What would be the producers, consumers, and topics in your scenario?
3.  What is the role of an offset for a Kafka consumer?
4.  Why is partitioning important for scalability in Kafka?
