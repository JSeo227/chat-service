package com.example.chat.chat_service.controller.dto;

import lombok.Data;

@Data
public class RoomForm {
    private String name;
    private String password;
    private Integer max;
}
