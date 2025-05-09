package com.example.chat.chat_service.repository.jpa;

import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Primary
@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaRoomRepository implements RoomRepository {

    private final EntityManager em;

    @Override
    public Room save(Room room) {
        if (room.getId() == null) {
            em.persist(room);
        } else {
            em.merge(room);
        }
        return room;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(em.find(Room.class, id));
    }

    @Override
    public List<Room> findAll() {
        return em.createQuery("select r from Room r", Room.class)
                .getResultList();
    }

    @Override
    public List<Room> findAllWithMembers() {
        return em.createQuery(
                "select distinct r from Room r" +
                        " join fetch r.memberRooms mr" +
                        " join fetch mr.member m", Room.class)
                .getResultList();

    }

}
