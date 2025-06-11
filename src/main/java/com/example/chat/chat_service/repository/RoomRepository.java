package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.room.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository {
    Room save(Room room);
    void delete(Room room);
    Optional<Room> findById(Long id);
    List<Room> findAll();
}
