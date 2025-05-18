package com.example.chat.chat_service.controller.advice;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalMemberAdvice {

    private final MemberService memberService;

    @ModelAttribute("member")
    public Member member(HttpServletRequest request) {

        try {
            MemberSession memberSession = SessionManager.getMemberSession(request);
            return memberService.findById(memberSession.getMemberId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
