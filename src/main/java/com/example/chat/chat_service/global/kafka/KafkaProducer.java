package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Message> template;

    public KafkaProducer(KafkaTemplate<String, Message> template) {
        this.template = template;
    }

    public void send(Message message) {
        log.info("Kafka Producer Message : {}", message);
        template.send("chat", message);
    }
}
