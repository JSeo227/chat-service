package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.chat.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Message> template;

    public KafkaProducer(KafkaTemplate<String, Message> template) {
        this.template = template;
    }

    public void send(Message message) {
        template.send("chat", message);
    }
}
