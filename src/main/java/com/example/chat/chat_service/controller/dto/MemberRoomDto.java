package com.example.chat.chat_service.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberRoomDto {

    private Long roomId;
    private String roomName;
    private Long memberId;
    private String memberName;

    @QueryProjection
    public MemberRoomDto(Long roomId, String roomName, Long memberId, String memberName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
