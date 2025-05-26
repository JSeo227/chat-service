package com.example.chat.chat_service.global.session;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ClientSession {
    private final Long memberId;
    private final String name;
}
