package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.chat.Message;
import com.example.chat.chat_service.domain.chat.Status;
import com.example.chat.chat_service.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @KafkaListener(topics = "chat", groupId = "chat-group")
    public void listen(Message message) {
        if (message.getStatus() == Status.TALK)
            messageRepository.save(message);
        messagingTemplate.convertAndSend(message.getDestination(), message);
        log.info("Kafka Consumer Message : {}", message);
    }
}
