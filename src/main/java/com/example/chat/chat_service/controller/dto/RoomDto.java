package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.room.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;

    @NotEmpty(message = "채팅방 이름은 필수 입력 값입니다.")
    private String name;

    private String password;

    private Integer currentMembers;

    @NotNull(message = "채팅방 최대인원은 필수 입력 값입니다.")  // <-- 수정
    @Max(value = 99, message = "화상채팅이 아닌, 일반채팅의 최대 인원은 99명 이하이어야 합니다.")
    @Min(value = 2, message = "최소 인원은 2명 이상이어야 합니다.")
    private Integer max;

    @NotNull(message = "채팅방 타입은 필수 입력 값입니다.")  // <-- 수정
    private RoomType type;
}
