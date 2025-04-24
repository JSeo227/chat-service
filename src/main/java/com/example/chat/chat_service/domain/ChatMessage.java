package com.example.chat.chat_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ChatMessage {

    @Id @GeneratedValue
    private Long id; //방번호

    private String sender;

    private String message;

    private String time; // 채팅 발송 시간

    private MessageStatus type; // 메시지 타입

}
