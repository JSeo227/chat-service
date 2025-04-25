package com.example.chat.chat_service.session;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SessionMember {

    private final String sessionId;
    private final String loginId;
    private final String name;
    private final Boolean isLogin;
}
