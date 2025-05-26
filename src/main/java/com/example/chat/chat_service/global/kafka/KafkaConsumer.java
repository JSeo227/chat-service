package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.message.Message;
import com.example.chat.chat_service.repository.mongo.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer { // 메시지를 구독

    private final MessageRepository messageRepository;

//    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
//    public void consume(Message message) {
//        log.info("Kafka로부터 메시지 수신: {}", message);
//        messageRepository.save(message);
//    }
}
