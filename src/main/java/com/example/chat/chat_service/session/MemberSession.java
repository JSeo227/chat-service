package com.example.chat.chat_service.session;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class MemberSession {
    private final String sessionId;
    private final String loginId;
    private final String name;
    private final Boolean isLogin;
}
