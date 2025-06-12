package com.example.chat.chat_service.controller.dto;

import com.example.chat.chat_service.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberForm {
    private Long id;

    @NotEmpty(message = "로그인 ID는 필수 입력 값입니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    public MemberForm(Member member) {
        this.id = member.getId();
        this.loginId = member.getLogin().getLoginId();
        this.password = member.getLogin().getPassword();
        this.name = member.getName();
    }
}
