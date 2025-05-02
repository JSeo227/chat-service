package com.example.chat.chat_service.session;

import jakarta.persistence.PreUpdate;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
public class MemberSession {
    private final Long memberId;
    private final String loginId;
    private final String password;
    private final String name;
    private final Boolean isLogin;
    private final LocalDateTime loginAt;

    public MemberSession(Long memberId, String loginId, String password, String name, Boolean isLogin) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.isLogin = isLogin;
        this.loginAt = LocalDateTime.now();
    }
}
