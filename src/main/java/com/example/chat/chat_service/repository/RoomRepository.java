package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.room.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository {
    ChatRoom save(ChatRoom room);
    ChatRoom findById(Long id);
    List<ChatRoom> findByName(String name);
    List<ChatRoom> findAll();
}
