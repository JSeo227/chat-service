package com.example.chat.chat_service.repository.jpa;

import com.example.chat.chat_service.domain.room.ChatRoom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaRoomRepository {

    private final EntityManager em;

    public void save(ChatRoom room) {}
}
