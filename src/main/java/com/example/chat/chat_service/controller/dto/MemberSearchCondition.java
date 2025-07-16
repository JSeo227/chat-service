package com.example.chat.chat_service.controller.dto;

import lombok.Data;

@Data
public class MemberSearchCondition {
    private Long id;
    private String loginId;
    private String password;
    private String name;
}
