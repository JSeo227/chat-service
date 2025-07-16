package com.example.chat.chat_service.global.advice;

import com.example.chat.chat_service.controller.dto.MemberDto;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class GlobalSessionAdvice {

    @ModelAttribute("memberSession")
    public MemberDto member() {

        try {
            MemberSession memberSession = SessionManager.getMemberSession();

            return MemberDto.builder()
                    .id(memberSession.getMemberId())
                    .loginId(memberSession.getLoginId())
                    .password(memberSession.getName())
                    .name(memberSession.getName())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
