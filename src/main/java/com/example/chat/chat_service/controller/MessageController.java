package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Message;
import com.example.chat.chat_service.domain.MessageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    @MessageMapping("/enter")   // 클라이언트가 보내는 주소
    @SendTo("/topic/public")    // 구독자에게 전파
    public Message enter(Message message) {
        if (message.getStatus() == MessageStatus.ENTER) {
            message.setContent(message.getSenderName() + "님이 입장하였습니다.");
        }
        return message;
    }

    @MessageMapping("/send")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
        if (message.getStatus() == MessageStatus.TALK) {
            return message;
        } else {
            Message invalidMessage = new Message();
            invalidMessage.setContent("잘못된 상태의 메시지입니다.");
            return invalidMessage;
        }
    }
}
