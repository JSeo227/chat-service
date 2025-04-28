package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.service.LoginService;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.session.MemberSession;
import com.example.chat.chat_service.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @PostMapping("/login")
    public String login(@ModelAttribute Login login, BindingResult result,
                        HttpServletRequest request) {

        if (result.hasErrors()) {
            return "/views/login/loginForm";
        }

        Member existingMember = memberService.findByLoginId(login.getLoginId());

        if (!loginService.isLoginId(login.getLoginId())) {
            result.rejectValue("loginId", "error.loginId", "로그인 아이디가 일치하지 않습니다.");
            return "/views/login/loginForm";
        }

        if (!loginService.isPassword(login.getPassword())) {
            result.rejectValue("password", "error.password", "비밀번호가 일치하지 않습니다.");
            return "/views/login/loginForm";
        }

        //세션 저장
        MemberSession memberSession = new MemberSession(
                UUID.randomUUID().toString(),
                existingMember.getId(),
                login.getLoginId(),
                login.getPassword(),
                existingMember.getName(),
                true
        );

        SessionManager.setMemberSession(request, memberSession);

        return "/views/rooms/roomListForm";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        MemberSession memberSession = SessionManager.getMemberSession(request);
        SessionManager.removeMemberSession(request, memberSession);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setHeader("Expires", "0"); // Proxies

        return "redirect:/login";
    }


}
