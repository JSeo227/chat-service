package com.example.chat.chat_service.session;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemberSession {
    private final String sessionId;                // 아이디
    private final Integer no;               // 번호
    private final String name;              // 이름
    private final Boolean isLogin;          // 로그인 여부
}