package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.MemberRepository;
import com.example.chat.chat_service.service.LoginService;
import com.example.chat.chat_service.session.SessionManager;
import com.example.chat.chat_service.session.MemberSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String login() {
        return "views/login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (bindingResult.hasErrors()) {
            return "views/login/loginForm";
        }

        HttpSession session = request.getSession();
        MemberSession memberSession = SessionManager.getMemberSession(request);
        session.setAttribute("loginId", loginMember.getId());
        return "redirect:/views/room/roomListForm";
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

    }

    @GetMapping("/loginInfo")
    public String loginInfo() {
        return "views/login/loginInfoForm";
    }
}
