package com.example.chat.chat_service.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signal {

    private Long roomId;
    private Long senderId;
    private String senderName;

    private String content;

    private Object candidate;
    private Object sdp; // Session Description Protocol

    private String type;
}