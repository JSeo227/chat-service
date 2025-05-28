package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.message.Message;
import com.example.chat.chat_service.domain.message.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageForm {

    private Long roomId;

    private Long senderId; // member id

    private String senderName; // member name

    private String content; // 메시지 내용

    private Status status; // ENTER, TALK, LEAVE

    public Message toEntity(MessageForm form) {
        return new Message(
                null,
                form.getRoomId(),
                form.getSenderId(),
                form.getSenderName(),
                form.getContent(),
                form.getStatus());
    }

}
