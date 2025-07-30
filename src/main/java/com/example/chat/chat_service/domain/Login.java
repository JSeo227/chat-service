package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {

    @Id
    private String loginId; //로그인 아이디

    private String password; //비밀번호

    private Boolean isLogin;
    
    @Override
    public String toString() {
        return "Login{" +
                "loginId='" + loginId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login that)) return false;
        return Objects.equals(loginId, that.loginId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(loginId);
    }

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
