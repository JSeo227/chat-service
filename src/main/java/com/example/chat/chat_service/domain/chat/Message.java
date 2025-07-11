package com.example.chat.chat_service.domain.chat;

import com.example.chat.chat_service.controller.dto.MessageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    private String id;

    private Long roomId;
    private Long senderId; // member id
    private String senderName; // member name
    private String content; // 메시지 내용
    private Status status; // ENTER, TALK, LEAVE
    private LocalDateTime createdAt;

    public Message(MessageDto messageDto) {
        this.roomId = messageDto.getRoomId();
        this.senderId = messageDto.getSenderId();
        this.senderName = messageDto.getSenderName();
        this.content = messageDto.getContent();
        this.status = messageDto.getStatus();
        this.createdAt = LocalDateTime.now();
    }
}
