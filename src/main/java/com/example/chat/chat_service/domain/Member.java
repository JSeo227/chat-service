package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty(message = "로그인 Id는 필수 입력 값입니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

}
