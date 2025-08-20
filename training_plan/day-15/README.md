# Day 15: Project: Building an End-to-End Data Pipeline

## Topic: Putting It All Together

Congratulations on making it to the final day of your training! Today, you'll put everything you've learned into practice by building a complete, end-to-end data pipeline.

### The Project

Your task is to build a pipeline that simulates tracking user activity on a website. The pipeline will:

1.  **Produce Events:** A Python script will generate fake user click events and send them to a Kafka topic.
2.  **Process Events:** A streaming engine (Spark or Flink) will read the events from Kafka, perform a simple transformation, and write the data to an Iceberg table.
3.  **Analyze Data:** You will then use SQL to run some analytical queries on the Iceberg table.

---

### Step 1: The Producer

Create a Python script (`activity_producer.py`) that generates user click events and sends them to a Kafka topic called `user_clicks`. Each event should be a JSON message with the following fields:

*   `user_id`: A unique user ID (e.g., `user_1`, `user_2`).
*   `page`: The page the user clicked on (e.g., `/home`, `/products`, `/cart`).
*   `event_ts`: The time of the click (as a timestamp string).

### Step 2: The Iceberg Table

Using either the Spark or Flink SQL clients, create an Iceberg table called `catalog.website.clicks` to store the clickstream data. The table should have columns for `user_id` (STRING), `page` (STRING), and `click_timestamp` (TIMESTAMP). Partition the table by `page`.

### Step 3: The Streaming Processor

You can implement the processor using either Spark or Flink.

#### **Option A: Spark Structured Streaming**
Create a PySpark script (`click_processor.py`) that:
1.  Reads from the `user_clicks` Kafka topic.
2.  Parses the JSON messages.
3.  Writes the data to the `clicks` Iceberg table using the `writeStream` API.

#### **Option B: Flink SQL**
In the Flink SQL Client, perform the following steps:
1.  Declare a `kafka_clicks` source table that maps to the `user_clicks` Kafka topic, specifying the correct JSON format.
2.  Declare the `website.clicks` Iceberg table as your sink (if you haven't already).
3.  Run a single `INSERT INTO ... SELECT` statement to start the continuous streaming job from the Kafka source to the Iceberg sink.

### Step 4: Analysis

Once your pipeline is running and has processed some data, use either the Spark or Flink SQL clients to answer the following questions:

1.  What are the most popular pages on the website?
2.  How many unique users have visited the site?
3.  What is the click activity for a specific user?

### Bonus Challenge

Modify your streaming processor to enrich the click events with user data. For example, you could have a separate `user_profiles` Iceberg table and join the clickstream with the user profiles to get the user's name and location. This is possible in both Spark and Flink.

This project will give you a solid foundation for building real-world data pipelines with Kafka, Spark, and Iceberg. Good luck!

## Final Thoughts

We have covered a lot of ground in the last 15 days. You should now have a good understanding of the fundamentals of Kafka and Iceberg and how to use them together to build powerful data applications.

The world of data engineering is constantly evolving, so keep learning, keep experimenting, and keep building!