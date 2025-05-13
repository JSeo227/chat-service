package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Message;
import com.example.chat.chat_service.domain.MessageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/enter")   // 클라이언트가 보내는 주소
    public void enter(Message message) {
        if (message.getStatus() == MessageStatus.ENTER) {
            message.setContent(message.getSenderName() + "님이 입장하였습니다.");
        }
        messagingTemplate.convertAndSend("/topic/chat/rooms/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/send")
    public void sendMessage(Message message) {
        if (message.getStatus() == MessageStatus.TALK) {
            messagingTemplate.convertAndSend("/topic/chat/rooms/" + message.getRoomId(), message);
        }
    }
}
