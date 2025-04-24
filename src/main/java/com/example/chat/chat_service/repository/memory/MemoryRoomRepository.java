package com.example.chat.chat_service.repository.memory;

import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class MemoryRoomRepository implements RoomRepository {

    private static long sequence = 0L;
    private static final Map<Long, Room> store = new HashMap<Long, Room>();

    @Override
    public Room save(Room room) {
        room.setId(++sequence);
        store.put(room.getId(), room);
        return room;
    }

    @Override
    public Room findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Room> findByName(String name) {
        return store.values().stream()
                .filter(r -> r.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clear(Room room) {
        store.clear();
    }
}
