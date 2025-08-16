# Kafka and Iceberg Learning Environment

## Purpose

This project provides a comprehensive, self-contained environment to learn and experiment with Apache Kafka and Apache Iceberg. It includes a complete Docker-based setup of all the necessary services and a structured 15-day training plan to guide you through the core concepts and practical applications.

## Key Components

The environment consists of the following services, all managed by Docker Compose:

*   **Apache Kafka:** A distributed event streaming platform.
*   **Apache Iceberg:** A modern, open table format for large analytic datasets.
*   **Nessie:** A transactional catalog for Iceberg that provides Git-like semantics for data.
*   **Apache Spark:** A unified analytics engine for large-scale data processing, used here to interact with Iceberg tables.
*   **Apache Flink:** A stream processing engine, also configured to interact with Iceberg.

## Getting Started

### Prerequisites

*   Docker
*   Docker Compose

### One-Time Setup

Before starting the services for the first time, you must download the Flink-Iceberg connector:

1.  Create the `flink-jars` directory: `mkdir flink-jars`
2.  Download the `iceberg-flink-runtime.jar` into the `flink-jars` directory. A compatible version can be found [here](https://repo1.maven.org/maven2/org/apache/iceberg/iceberg-flink-runtime-1.18/1.4.3/iceberg-flink-runtime-1.18-1.4.3.jar).

### Running the Environment

*   **To start all services:** Run the following command in the project root. The `-d` flag runs the containers in detached mode.
    ```bash
    docker-compose up -d
    ```
*   **To stop all services:**
    ```bash
    docker-compose down
    ```

## Folder Structure

*   `./docker-compose.yml`: The main configuration file that defines and connects all the services (Kafka, Spark, Flink, etc.).
*   `./training_plan/`: A 15-day structured curriculum designed to teach you Kafka and Iceberg. Each day's folder contains a `README.md` with explanations, real-world examples, and practical exercises.
*   `./flink-jars/`: This folder holds the necessary connector JAR to allow Flink to communicate with Iceberg.

## Accessing Services

Once the environment is running, you can access the various services and UIs:

*   **Kafka Broker:** `localhost:9092` (for producers/consumers)
*   **Nessie UI (Iceberg Catalog):** `http://localhost:19120`
*   **Flink Web UI:** `http://localhost:8081`
*   **Spark SQL Shell:**
    ```bash
    docker exec -it spark-iceberg spark-sql
    ```
