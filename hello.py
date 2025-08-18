from kafka import KafkaProducer

if __name__ == "__main__":
    producer = KafkaProducer(bootstrap_servers='127.0.0.1:9092')
    producer.send(topic='ubd', value=b'Hello, Kafka!', key="key1")
    producer.flush()
    print("Message sent to Kafka topic ")
