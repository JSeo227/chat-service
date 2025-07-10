package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.chat.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
