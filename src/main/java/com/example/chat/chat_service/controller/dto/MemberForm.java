package com.example.chat.chat_service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberForm {

    private Long id;

    @NotEmpty(message = "로그인 ID는 필수 입력 값입니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;
}
