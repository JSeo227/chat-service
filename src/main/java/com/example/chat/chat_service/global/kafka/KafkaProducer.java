package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.chat.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Message> kafkaTemplate;
    private static final String TOPIC = "chat-messages";

//    public void sendMessage(Message message) {
//        log.info("Kafka에 메시지 전송: {}", message);
//        kafkaTemplate.send(TOPIC, message);
//    }
}
