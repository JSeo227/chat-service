package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    @MessageMapping("/enter")
    @SendTo("/topic/public")
    public String enter(Message message) throws Exception {
        return message.getSenderName();
    }

    @MessageMapping("/send")     // 클라이언트가 보내는 주소
    @SendTo("/topic/public")            // 사람에게 전파
    public String sendMessage(Message message) throws Exception {
        return message.getContent();
    }
}
