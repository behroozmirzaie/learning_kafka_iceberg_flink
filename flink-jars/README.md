# Flink Iceberg Dependencies

This directory contains all the JAR files needed to run Apache Iceberg with Apache Flink in our learning environment.

## Why These JARs Are Needed

### Core Iceberg Support
- **iceberg-flink-runtime-1.18-1.9.2.jar** - Main Iceberg connector for Flink
  - Provides Iceberg table format support
  - Enables CREATE CATALOG, CREATE TABLE, INSERT, SELECT operations
  - Bridges Flink SQL with Iceberg APIs

### Hadoop Ecosystem (Storage Layer)
- **hadoop-common-3.3.2.jar** - Core Hadoop libraries
  - Provides `org.apache.hadoop.conf.Configuration` class
  - Essential configuration management for distributed systems
  - Required by Iceberg for storage abstraction

- **hadoop-hdfs-client-3.3.2.jar** - HDFS client libraries
  - Contains `org.apache.hadoop.hdfs.HdfsConfiguration`
  - Provides distributed file system abstractions
  - Even when using S3, Iceberg relies on Hadoop's filesystem APIs

- **hadoop-aws-3.3.2.jar** - Hadoop S3 integration
  - Enables Hadoop to work with S3-compatible storage (MinIO)
  - Provides S3A filesystem implementation
  - Required for `s3a://` warehouse paths

### AWS SDK (Cloud Storage)
- **aws-java-sdk-bundle-1.11.1026.jar** - AWS SDK v1
  - Legacy AWS SDK for backward compatibility
  - Used by hadoop-aws for S3 operations

- **bundle-2.20.18.jar** - AWS SDK v2
  - Modern AWS SDK with better performance
  - Required by newer Iceberg versions for S3 operations
  - Provides `software.amazon.awssdk.*` classes

### Flink Filesystem Connectors
- **flink-s3-fs-hadoop-1.18.1.jar** - Flink S3 filesystem connector
  - Enables Flink to directly access S3-compatible storage
  - Complements Iceberg's S3 support
  - Provides native Flink S3 integration

## What This Enables

With these JARs, you can:

1. **Use Nessie Catalog** - Connect to Nessie for Git-like data versioning
2. **Store Data in MinIO/S3** - Use S3-compatible object storage as warehouse
3. **Monitor in S3 UI** - View actual data files in MinIO console
4. **Track Commits in Nessie** - See table versions and branches in Nessie UI
5. **Full ACID Operations** - INSERT, UPDATE, MERGE, time-travel queries

## Usage

These JARs are automatically mounted into the Flink container at `/opt/flink/usrlib/`.
The docker-compose setup copies them to `/opt/flink/lib/` on startup.

## Catalog Configuration Examples

### Simple Local Storage (for learning)
```sql
CREATE CATALOG catalog WITH (
  'type' = 'iceberg',
  'catalog-impl' = 'org.apache.iceberg.hadoop.HadoopCatalog',
  'warehouse' = 'file:///tmp/iceberg-warehouse'
);
```

### Full S3 + Nessie Setup (production-like)
```sql
-- Use this configuration if you have all AWS SDK v2 dependencies
CREATE
CATALOG catalog WITH (
  'type' = 'iceberg',
  'catalog-impl' = 'org.apache.iceberg.nessie.NessieCatalog',
  'uri' = 'http://nessie:19120/api/v1',
  'ref' = 'main',
  'warehouse' = 's3a://warehouse',
  'io-impl' = 'org.apache.iceberg.aws.s3.S3FileIO',
  's3.endpoint' = 'http://minio:9000',
  's3.access-key-id' = 'admin',
  's3.secret-access-key' = 'password',
  's3.region' = 'us-east-1',
  's3.path-style-access' = 'true'
);
```

### Alternative S3 + Nessie Setup (more compatible)
```sql
-- Use this if you encounter AWS SDK version issues
CREATE CATALOG catalog WITH (
  'type' = 'iceberg',
  'catalog-impl' = 'org.apache.iceberg.nessie.NessieCatalog',
  'uri' = 'http://nessie:19120/api/v1',
  'ref' = 'main',
  'warehouse' = 's3a://warehouse'
);
```

### Working with Nessie + File Storage
```sql
-- Most compatible setup for learning
CREATE CATALOG catalog WITH (
  'type' = 'iceberg',
  'catalog-impl' = 'org.apache.iceberg.nessie.NessieCatalog',
  'uri' = 'http://nessie:19120/api/v1',
  'ref' = 'main',
  'warehouse' = 'file:///tmp/iceberg-warehouse'
);
```