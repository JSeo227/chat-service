package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.chat.Message;
import com.example.chat.chat_service.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final MessageRepository messageRepository;

    public KafkaConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "chat", groupId = "chat-group")
    public void listen(Message message) {
//        messageRepository.save(message);
        System.out.println("message = " + message);
    }
}
