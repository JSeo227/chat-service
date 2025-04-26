package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Login {

    @Id
    @Column(name = "login_id")
    //    @NotEmpty(message = "로그인 Id는 필수 입력 값입니다.")
    private String loginId;

    //    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "login")
    private Member member;

    // 생성 메서드
    public static Login createLogin(String loginId, String password, Member member) {
        Login login = new Login();
        login.loginId = loginId;
        login.password = password;
        login.member = member;
        return login;
    }

    // 비밀번호 확인 메서드
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
