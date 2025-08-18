
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * A simple Java application to create a Kafka topic.
 */
public class CreateKafkaTopic {

    public static void main(String[] args) {
        // Configuration properties for the AdminClient
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Define the new topic details
        String topicName = "my-java-topic";
        int numPartitions = 3;
        short replicationFactor = 1; // In our single-broker setup, this must be 1

        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

        // Create an AdminClient instance using try-with-resources to ensure it's closed properly
        try (AdminClient adminClient = AdminClient.create(props)) {
            System.out.println("Connecting to Kafka and creating topic: " + topicName);
            
            // Send the request to create the topic
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            
            System.out.println("Topic '" + topicName + "' created successfully!");

        } catch (ExecutionException e) {
            // Check if the topic already exists
            if (e.getCause() instanceof org.apache.kafka.common.errors.TopicExistsException) {
                System.out.println("Topic '" + topicName + "' already exists.");
            } else {
                System.err.println("Error creating topic '" + topicName + "': " + e.getMessage());
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Topic creation was interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
