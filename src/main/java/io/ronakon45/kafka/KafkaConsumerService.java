package io.ronakon45.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConsumerService {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    
    private static final String TOPIC_NAME = "your-topic-name";

    @KafkaListener(topics = TOPIC_NAME, groupId = "group-id")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
        // Process the received message
    }

    @KafkaListener(topics = "topicName", groupId = "foo" )
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }

    @KafkaListener(topics = "your-topic-name", groupId = "your-group-id")
    public void consumeFromBeginning(@Payload String message,
                                     @Header(KafkaHeaders.OFFSET) int offset) {
        System.out.println("Received message: " + message + ", offset: " + offset);
    }

    public void configureConsumer() {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "your-group-id");
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Set to consume from beginning

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfig);

        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory);

        KafkaMessageListenerContainer<String, String> container =
                new KafkaMessageListenerContainer<>(consumerFactory, new ContainerProperties("your-topic-name"));
        container.setupMessageListener((MessageListener<String, String>) record -> {
            System.out.println("Received message: " + record.value() + ", offset: " + record.offset());
        });
        container.start();
    }

}