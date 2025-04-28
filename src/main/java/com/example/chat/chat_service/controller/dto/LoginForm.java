package com.example.chat.chat_service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

    private Long id;

    private String loginId;

    private String password;

    private String name;

    private Boolean isLogin;
}
