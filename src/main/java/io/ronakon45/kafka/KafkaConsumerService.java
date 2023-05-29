package io.ronakon45.kafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final String TOPIC_NAME = "your-topic-name";

    @KafkaListener(topics = TOPIC_NAME, groupId = "group-id")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
        // Process the received message
    }
}