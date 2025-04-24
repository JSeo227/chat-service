package com.example.chat.chat_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Message {

    @Id @GeneratedValue
    private Long id; //방번호

    private String sender;

    private String message;
}
