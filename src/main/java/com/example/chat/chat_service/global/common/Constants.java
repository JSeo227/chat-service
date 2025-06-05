package com.example.chat.chat_service.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Constants {
    MEMBER_SESSION("memberSession");
    private final String name;
}
