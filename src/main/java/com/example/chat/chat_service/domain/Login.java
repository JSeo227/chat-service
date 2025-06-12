package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
public class Login {

    @Id
    private String loginId; //로그인 아이디

    private String password; //비밀번호

    private Boolean isLogin;

    // equals - 프록시와 비교할 수 있도록 id 기반으로만 비교
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;

        Login login = (Login) o;
        return loginId != null && loginId.equals(login.getLoginId());
    }

    // hashCode - equals와 일치하게 id 기반으로
    @Override
    public int hashCode() {
        return Objects.hash(loginId);
    }

    // toString - 지연 로딩 방지 위해 password, isLogin만 사용 (또는 전부 제거)
    @Override
    public String toString() {
        return "Login(loginId=" + loginId + ")";
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
