package com.example.chat.chat_service.controller.dto;

import lombok.Data;

@Data
public class CheckPasswordDto {
    private Long roomId;
    private String password;
}