package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class Login {

    @Id
    @Column(name = "login_id")
    private String loginId; //로그인 아이디

    private String password; //비밀번호

    //==생성 메서드==//
    public static Login createLogin(String loginId, String password) {
        Login login = new Login();
        login.setLoginId(loginId);
        login.setPassword(password);
        return login;
    }

    //==로직 메서드==//

    // 비밀번호 확인
    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
