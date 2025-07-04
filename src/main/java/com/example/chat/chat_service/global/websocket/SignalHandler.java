package com.example.chat.chat_service.global.websocket;

import com.example.chat.chat_service.domain.chat.Signal;
import com.example.chat.chat_service.global.common.Constants;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.service.RoomSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class SignalHandler extends TextWebSocketHandler { // json 이여서 Text

    private final RoomSessionManager roomSessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // SDP Offer message
    private static final String MSG_TYPE_OFFER = "OFFER";
    // SDP Answer message
    private static final String MSG_TYPE_ANSWER = "ANSWER";
    // New ICE (Interactive Connectivity Establishment) Candidate message
    private static final String MSG_TYPE_ICE = "ICE";
    // join a room data message
    private static final String MSG_TYPE_ENTER = "ENTER";
    // leave a room data message
    private static final String MSG_TYPE_LEAVE = "LEAVE";

    // 연결 성공
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[ws] Session has been connect with status [{}]", session);
        MemberSession memberSession = (MemberSession) session.getAttributes().get(Constants.MEMBER_SESSION.getName());
        if (memberSession == null) {
            session.close();
            return;
        }
        sendMessage(session, new Signal(1L, memberSession.getMemberId(), memberSession.getName(), null, null, MSG_TYPE_ENTER));
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
            log.info("signal={}", signal);

            Long roomId = signal.getRoomId();
            Long memberId = signal.getSenderId();
            String memberName = signal.getSenderName();

            switch (signal.getType()) {
                case MSG_TYPE_ENTER -> {
                    log.info("enter the room");
                    roomSessionManager.setClients(roomId, memberId, session);
                }

                case MSG_TYPE_LEAVE -> {
                    log.info("leave the room");
                    roomSessionManager.removeClients(roomId, memberId);
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
            log.debug("An error occurred during message processing: {}", e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, Signal signal) {
        log.info("message={}", signal);
        try {
            String json = objectMapper.writeValueAsString(signal);
            log.info("json={}", json);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.debug("An error occurred during message sending: {}", e.getMessage());
        }
    }
}
