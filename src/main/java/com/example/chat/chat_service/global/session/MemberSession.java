package com.example.chat.chat_service.global.session;

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
    private final Boolean checked;
    private final String name;
    private final Boolean isLogin;
    private final LocalDateTime loginAt;
}
