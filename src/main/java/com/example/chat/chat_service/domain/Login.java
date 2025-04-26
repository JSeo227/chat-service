package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class Login {

    @Id
    @Column(name = "login_id")
    @NotEmpty(message = "로그인 Id는 필수 입력 값입니다.")
    private String loginId; //로그인 아이디

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password; //비밀번호

    //양방향
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "login")
//    private Member member; // 회원

    //==생성 메서드==//
    public static Login createLogin(String loginId, String password) {
        Login login = new Login();
        login.setLoginId(loginId);
        login.setPassword(password);
        return login;
    }

    //==연관관계 메서드==//

    //==로직 메서드==//

    // 비밀번호 확인 메서드
    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
