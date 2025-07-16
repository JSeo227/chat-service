package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}