package com.example.chat.chat_service.controller.advice;

import com.example.chat.chat_service.controller.dto.MemberForm;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalMemberAdvice {

    @ModelAttribute("memberSession")
    public MemberForm member() {

        try {
            MemberSession memberSession = SessionManager.getMemberSession();

            return MemberForm.builder()
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
