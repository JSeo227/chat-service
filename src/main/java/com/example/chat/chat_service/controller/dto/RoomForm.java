package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.room.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomForm {
    private String name;
    private String password;
    private Integer max;
    private RoomType type;
}
