package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.Message;
import com.example.chat.chat_service.domain.MessageStatus;
import com.example.chat.chat_service.domain.room.Room;
//import com.example.chat.chat_service.global.kafka.KafkaProducer;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

//    private final KafkaProducer kafkaProducer;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;
    private final MemberService memberService;

    /**
     * 채팅방 입장 처리
     * 클라이언트가 /chat/enter로 메시지 전송 시 호출
     *
     * MessageController는 WebSocket용 컨트롤러, 따라서 HttpServletRequest 사용 X
     * SimpMessageHeaderAccessor를 사용해 직접 session에 값을 저장해야함
     */
    @MessageMapping("/chat/enter")
    public void enter(@Payload Message message, SimpMessageHeaderAccessor header) {
        log.info("enter message = {}", message);
        if (message.getStatus() == MessageStatus.ENTER) {
            message.setContent(message.getSenderName() + "님이 입장하였습니다.");

            // WebSocket Session에 데이터 저장
            header.getSessionAttributes().put("roomId", message.getRoomId());
            header.getSessionAttributes().put("memberId", message.getSenderId());
        }
//        kafkaProducer.send("/topic/chat/room/" + message.getRoomId(), message);
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    /**
     * 채팅 메시지 전송 처리
     */
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload Message message) {
        log.info("send message = {}", message);

        if (message.getStatus() == MessageStatus.TALK) {
//            kafkaProducer.send("/topic/chat/room/" + message.getRoomId(), message);
            messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
        }
    }

    /**
     * 채팅방 퇴장 처리 (창 닫기, 뒤로가기 등)
     * HttpServletRequest를 통해 현재 세션의 회원정보와 참여중인 방 ID를 조회하여 퇴장 처리
     */
    @EventListener
    public void exit(SessionDisconnectEvent event) {
        log.info("exit event = {}", event);

        // WebSocket Session에서 데이터 불러오기
        SimpMessageHeaderAccessor header = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Long roomId = (Long) header.getSessionAttributes().get("roomId");
        Long memberId = (Long) header.getSessionAttributes().get("memberId");

        log.info("roomId = {}, memberId = {}", roomId, memberId);

        Room room = roomService.findRoomById(roomId);
        Member member = memberService.findById(memberId);

        roomService.exitRoom(roomId, member);

        Message message = new Message(
            roomId,
            memberId,
            member.getName(),
            member.getName() + "님이 퇴장하였습니다.",
            MessageStatus.LEAVE
        );

        log.info("exit message = roomId={}, memberId={}, memberName={}, message={}",
                roomId, memberId, member.getName(), message.getContent());
//        kafkaProducer.send("/topic/chat/room/" + roomId, message);
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, message);
    }
}
