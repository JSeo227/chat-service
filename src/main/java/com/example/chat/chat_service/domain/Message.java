package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Long roomId;

    private Long senderId;

    private String content;

    private String time; // 채팅 발송 시간

    @Enumerated(EnumType.STRING)
    private MessageStatus status; // ENTER, TALK, LEAVE
}
