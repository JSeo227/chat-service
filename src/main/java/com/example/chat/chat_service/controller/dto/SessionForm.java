package com.example.chat.chat_service.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionForm {
    private Long memberId;
    private String name;
}
