package com.example.chat.chat_service.domain.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//    @Enumerated(EnumType.STRING)
    private Status status; // ENTER, TALK, LEAVE
}
