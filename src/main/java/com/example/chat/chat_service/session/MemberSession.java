package com.example.chat.chat_service.session;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemberSession {
    private final String sessionId;         // 세션 아이디
    private final String loginId;           // 아이디
    private final String loginName;         // 이름
    private final String loginYN;           // 로그인 여부 (Y.로그인/ N.비로그인)
}
