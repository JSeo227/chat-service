package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.room.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository {
    Room save(Room room);
    Room findById(Long id);
    List<Room> findByName(String name);
    List<Room> findAll();
}
