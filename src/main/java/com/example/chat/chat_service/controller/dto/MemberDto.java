package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String loginId;
    private String password;
    private String name;

    public MemberDto(Member member) {
        this.loginId = member.getLogin().getLoginId();
        this.password = member.getLogin().getPassword();
        this.name = member.getName();
    }
}
