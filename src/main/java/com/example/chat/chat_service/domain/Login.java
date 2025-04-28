package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class Login {

    @Id
    private String loginId; //로그인 아이디

    private String password; //비밀번호

    private Boolean isLogin;

    //==생성 메서드==//
    public static Login createLogin(String loginId, String password, Boolean isLogin) {
        Login login = new Login();
        login.setLoginId(loginId);
        login.setPassword(password);
        login.setIsLogin(isLogin);
        return login;
    }

    //==로직 메서드==//

    public boolean checkLoginId(String inputLoginId) {
        return loginId.equals(inputLoginId);
    }

    // 비밀번호 확인
    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
