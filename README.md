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

## Architecture Overview

The following diagram illustrates how the major components in this environment interact when you query an Iceberg table.

```mermaid
graph TD
    subgraph User
        A[Flink/Spark SQL Client]
    end

    subgraph Processing Layer
        B[Flink / Spark Engine]
    end

    subgraph Catalog
        C[Nessie Service]
    end

    subgraph Storage Layer (MinIO / S3)
        D[Iceberg Metadata <br><i>metadata.json, manifests</i>]
        E[Iceberg Data <br><i>.parquet files</i>]
    end

    A -- 1. SQL Query --> B
    B -- 2. 'Where is the table metadata?' --> C
    C -- 3. 'It is at this path in storage' --> B
    B -- 4. Reads metadata files --> D
    B -- 5. Reads data files --> E
    B -- 6. Returns Result --> A

    style C fill:#cde4ff,stroke:#6a8ebf
    style D fill:#d5f0d5,stroke:#7aa87a
    style E fill:#fff2cc,stroke:#d6b656
```

### How It Works

1.  A user submits a **SQL Query** to a processing engine like Spark or Flink.
2.  The engine needs to understand the table, so it asks the **Nessie Catalog**: "Where is the current metadata for this table?"
3.  Nessie's only job is to provide the path to the current metadata file (e.g., `s3://warehouse/.../table.metadata.json`). It acts as the source of truth for the table's state.
4.  The engine reads the **Iceberg Metadata** files from MinIO (our S3-compatible storage). These files describe the table's schema, partitions, and history.
5.  From the metadata, the engine discovers which specific **Iceberg Data** files (.parquet) it needs to read from MinIO to satisfy the query.
6.  The engine processes the data and returns the final result to the user.

This architecture decouples the processing engines (Spark/Flink) from the catalog (Nessie) and the storage (MinIO), allowing them to scale and evolve independently.

### Viewing the Diagram
The diagram above is in the [Mermaid](https://mermaid.js.org/) format. It is already in the correct format for a Markdown file (`.md`). When you view this `README.md` file on a platform like GitHub, GitLab, or in many modern text editors, it will be automatically rendered as a visual diagram.

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
