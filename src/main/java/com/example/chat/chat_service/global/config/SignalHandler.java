package com.example.chat.chat_service.global.config;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.chat.Signal;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.service.RoomService;
import com.example.chat.chat_service.service.RoomSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
//시그널 서버 역할을 담당하는 클래스
public class SignalHandler extends TextWebSocketHandler { // json이여서 Text

    private final MemberService memberService;
    private final RoomService roomService;
    private final RoomSessionManager roomSessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // SDP Offer message
    private static final String MSG_TYPE_OFFER = "offer";
    // SDP Answer message
    private static final String MSG_TYPE_ANSWER = "answer";
    // New ICE(Interactive Connectivity Establishment) Candidate message
    private static final String MSG_TYPE_ICE = "ice";
    // join room data message
    private static final String MSG_TYPE_ENTER = "enter";
    // leave room data message
    private static final String MSG_TYPE_LEAVE = "leave";

    // 연결 성공
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[ws] Session has been connect with status [{}]", session);
        MemberSession memberSession = SessionManager.getMemberSession();
        sendMessage(session, new Signal(1L, memberSession.getMemberId(), memberSession.getName(), null, null, MSG_TYPE_ENTER));
    }

    private void sendMessage(WebSocketSession session, Signal signal) {
        log.info("message={}", signal);
        try {
            String json = objectMapper.writeValueAsString(signal);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.debug("An error occurred: {}", e.getMessage());
        }
    }

    // 연결 끊김
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("[ws] Session has been closed with status [{} {}]", status, session);
    }

    // 메시지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("a message has been received");

        try {
            Signal signal = objectMapper.readValue(message.getPayload(), Signal.class);

            Long roomId = signal.getRoomId();
            Long memberId = signal.getSenderId();
            String memberName = signal.getSenderName();

            switch (signal.getType()) {
                case MSG_TYPE_ENTER -> {
                    Member member = memberService.findById(memberId);
                    Room room = roomService.findRoomById(roomId);
                    MemberRoom memberRoom = MemberRoom.createForEnter(member, room);

                    roomSessionManager.setClients(roomId, memberId, session);
                    roomService.enterRoom(roomId, memberRoom);
                }

                case MSG_TYPE_LEAVE -> {
                    roomSessionManager.removeClients(roomId, memberId);
                    roomService.exitRoom(roomId, memberService.findById(memberId));
                }

                case MSG_TYPE_OFFER, MSG_TYPE_ANSWER, MSG_TYPE_ICE -> {
                    Map<Long, WebSocketSession> clients = roomSessionManager.getClients(roomId);

                    for (Map.Entry<Long, WebSocketSession> client : clients.entrySet()) {
                        if (!client.getKey().equals(memberId)) { // 자신이 아니면 메시지를 보냄
                            sendMessage(client.getValue(), new Signal(
                                    roomId,
                                    memberId,
                                    memberName,
                                    signal.getCandidate(),
                                    signal.getSdp(),
                                    signal.getType()
                            ));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("An error occurred: {}", e.getMessage());
        }
    }
}
