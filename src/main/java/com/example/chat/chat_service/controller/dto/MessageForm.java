package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.chat.Message;
import com.example.chat.chat_service.domain.chat.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageForm {
    private Long roomId;
    private Long senderId; // member id
    private String senderName; // member name
    private String content; // 메시지 내용
    private Status status; // ENTER, TALK, LEAVE
}
