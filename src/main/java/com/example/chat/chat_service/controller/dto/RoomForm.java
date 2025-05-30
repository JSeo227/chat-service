package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.room.RoomType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomForm {
    private String name;
    private String password;
    private Integer max;
    private RoomType type;
}
