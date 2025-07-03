package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.controller.dto.LoginDto;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.service.LoginService;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("login") LoginDto form) {
        return "views/login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") LoginDto form, BindingResult result) {

        if (result.hasErrors()) {
            return "views/login/loginForm";
        }

        Member existingMember = memberService.findByLoginId(form.getLoginId());

        if (!loginService.isLoginId(form.getLoginId())) {
            log.info("로그인 아이디가 일치하지 않습니다.");
            result.rejectValue("loginId", "error.loginId", "로그인 아이디가 일치하지 않습니다.");
            return "views/login/loginForm";
        }

        if (!loginService.isPassword(form.getPassword())) {
            log.info("비밀번호가 일치하지 않습니다.");
            result.rejectValue("password", "error.password", "비밀번호가 일치하지 않습니다.");
            return "views/login/loginForm";
        }

        //로그인 성공 isLogin : false -> true
        loginService.setLoginStatusTrue(form.getLoginId(), true);

        //세션 저장
        MemberSession memberSession = MemberSession.builder()
                .memberId(existingMember.getId())
                .loginId(form.getLoginId())
                .checked(form.getChecked())
                .name(existingMember.getName())
                .isLogin(true)
                .loginAt(LocalDateTime.now())
                .build();

        SessionManager.setMemberSession(memberSession);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        MemberSession memberSession = SessionManager.getMemberSession();
        loginService.setLoginStatusTrue(memberSession.getLoginId(), false);
        SessionManager.removeMemberSession(memberSession);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setHeader("Expires", "0"); // Proxies

        return "redirect:/login";
    }

}
