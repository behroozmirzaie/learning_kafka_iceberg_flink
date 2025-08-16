# Day 13: Advanced Kafka Concepts

## Topic: Topic Configuration, Retention, and Compaction

We've covered the basics of Kafka, but there are many configuration options that allow you to fine-tune its behavior for different use cases. Today, we'll look at some of the most important ones.

### Topic Configuration

You can configure many aspects of a topic, such as the number of partitions, the replication factor, and retention policies.

*   **Replication Factor:** The replication factor determines how many copies of each partition are stored in the cluster. A replication factor of 3 is common for production environments. This ensures that if one broker fails, the data is still available on other brokers.
*   **Retention Policies:** Kafka doesn't keep messages forever. By default, it retains messages for a certain period of time (e.g., 7 days) or until the topic reaches a certain size. You can configure these retention policies on a per-topic basis.

### Log Compaction

For some use cases, you don't need to keep the entire history of messages; you only need the most recent value for each key. This is where log compaction comes in.

When you enable log compaction for a topic, Kafka will periodically clean up the topic by removing older messages that have the same key as a more recent message. This ensures that the topic only contains the latest value for each key.

### Real-World Example

*   **Retention Policies:** Consider a topic that stores application logs. You might want to keep these logs for 30 days for debugging purposes. After 30 days, the logs can be deleted to save space.

*   **Log Compaction:** Consider a topic that stores the current location of every driver in a ride-sharing app. You don't need to know where the driver was 5 minutes ago; you only need their current location. By enabling log compaction on this topic, you can ensure that it only stores the most recent location for each driver, which can significantly reduce the size of the topic.

## Training Questions

1.  Create a new topic called `important_events` with a replication factor of 1 (since we only have one broker) and a retention period of 1 hour.
2.  Create another topic called `user_profiles` and enable log compaction for it.
3.  Write a producer that sends a few messages to the `user_profiles` topic with the same key but different values. For example:
    *   Key: `user123`, Value: `{ "name": "Alice", "email": "alice@example.com" }`
    *   Key: `user123`, Value: `{ "name": "Alice", "email": "new_alice@example.com" }`
4.  Use a consumer to read from the `user_profiles` topic. What do you observe? After some time, you should only see the last message for `user123`.
5.  When would you use a time-based retention policy versus a size-based retention policy?
