package com.example.chat.chat_service.repository.mongo;

import com.example.chat.chat_service.domain.chat.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final MongoTemplate template;

    public Message save(Message message) {
        return template.save(message);
    }

    public List<Message> findAll() {
        return template.findAll(Message.class);
    }
}
