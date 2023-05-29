package io.ronakon45.kafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {

    private static final String TOPIC_NAME = "your-topic-name";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/messages")
    public CompletableFuture<String> sendMessage(@RequestBody String message) {
       AtomicReference<String> msg = new AtomicReference<>("a");
       CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, message);
    
       return future.thenApply(result -> {
           msg.set("Sent message=[" + message +
           "] with offset=[" + result.getRecordMetadata().offset() + "]"); // Update the value of msg
           System.out.println("Sent message=[" + message +
                   "] with offset=[" + result.getRecordMetadata().offset() + "]");
           return msg.get();
       });
    }
    
    
    
    
}
