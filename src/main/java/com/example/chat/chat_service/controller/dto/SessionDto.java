package com.example.chat.chat_service.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionDto {
    private Long memberId;
    private String loginId;
    private Boolean checked;
    private String name;
}
