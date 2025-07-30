package com.example.chat.chat_service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JoinDto {

    private String loginId;

    private String password;

    private String name;
}
