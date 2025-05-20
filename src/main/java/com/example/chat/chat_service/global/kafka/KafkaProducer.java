/*
package com.example.chat.chat_service.global.kafka;

import com.example.chat.chat_service.domain.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void send(String topic, Message message) {
        log.info("전송할 주소: {}", topic);
        log.info("전송할 메시지: {}", message);
        kafkaTemplate.send(topic, message);
    }
}
*/
