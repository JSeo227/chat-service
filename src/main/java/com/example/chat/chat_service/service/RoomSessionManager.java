package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.room.Room;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomSessionManager {

    // 화상채팅 회원 목록 -> (방, (회원, 세션))
    private final Map<Long, Map<Long, WebSocketSession>> roomSessions = new HashMap<>();

    // memberList에 클라이언트 불러오기
    public Map<Long, WebSocketSession> getClients(Long roomId) {
        return roomSessions.getOrDefault(roomId, new HashMap<>());
    }

    // memberList에 클라이언트 저장
    public void setClients(Long roomId, Long memberId, WebSocketSession session) {
        roomSessions
                .computeIfAbsent(roomId, k -> new HashMap<>())
                .put(memberId, session);
    }

    // memberList에 클라이언트 삭제
    public void removeClients(Long roomId, Long memberId) {
        Map<Long, WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(memberId);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }
}
