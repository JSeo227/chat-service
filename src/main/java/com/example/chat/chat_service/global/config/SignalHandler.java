package com.example.chat.chat_service.global.config;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.message.SignalMessage;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
//시그널 서버 역할을 담당하는 클래스
public class SignalHandler extends TextWebSocketHandler { // 텍스트 기반(JSON)으로 데이터를 주고 받음

    private final RoomService roomService;
    private final MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Long, Room> rooms;

    // 방 ID → 참여자의 세션 리스트
    private final Map<Long, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    // message types, used in signalling:
    // SDP Offer message
    private static final String TYPE_OFFER = "offer";
    // SDP Answer message
    private static final String TYPE_ANSWER = "answer";
    // New ICE(Interactive Connectivity Establishment) Candidate message
    private static final String TYPE_ICE_CANDIDATE = "ice";
    // Enter Room message
    private static final String TYPE_ENTER = "enter";
    // Leave Room message
    private static final String TYPE_LEAVE = "leave";

    // 소켓 연결 성공
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹소켓이 연결되면 -> 클라이언트에게 메시지 보냄
        // Boolean.toString(!rooms.isEmpty()) = true -> 클라이언트가 협상
        // P2P 연결을 설정하려면 상대방을 기다려야함

        log.info("Session has been connect with status [{}]", session);
        sendMessage(session, new SignalMessage(null, null, null, null, null, TYPE_ENTER));
    }

    private void sendMessage(WebSocketSession session, SignalMessage message) {
        log.info("signal message : {}", message);
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.debug("An error occurred: {}", e.getMessage());
        }
    }

    // 소켓 연결 끊김
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Session has been closed with status [{} {}]", status, session);
    }

    // 소켓 메시지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {

            SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

            Long roomId = signalMessage.getRoomId();
            Long memberId = signalMessage.getSenderId();

            Room room = roomService.findRoomById(roomId);
            Member member = memberService.findById(memberId);

            switch (signalMessage.getType()) {
                case TYPE_OFFER:
                case TYPE_ANSWER:
                case TYPE_ICE_CANDIDATE:
                    forwardMessageToOthers(roomId, session, new SignalMessage());
                    break;
                case TYPE_ENTER:
                    MemberRoom memberRoom = MemberRoom.createForEnter(member, room);
                    roomService.enterRoom(roomId, memberRoom);
                    log.info("member enter room : {}", member);
                    break;
                case TYPE_LEAVE:
                    roomService.exitRoom(roomId, member);
                    log.info("member leave room : {}", member);
                    break;

                default:
                    log.error("Unknown message type: {}", signalMessage.getType());
                    break;
            }

        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    private void forwardMessageToOthers(Long roomId, WebSocketSession senderSession, SignalMessage message) {
        List<WebSocketSession> sessions = sessionMap.get(roomId);
        if (sessions == null) return;

        for (WebSocketSession session : sessions) {
            if (!session.equals(senderSession) && session.isOpen()) {
                try {
                    String json = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(json));
                    log.info("Forwarded message to session [{}]", session.getId());
                } catch (IOException e) {
                    log.warn("Failed to forward message to session [{}]: {}", session.getId(), e.getMessage());
                }
            }
        }
    }
}
