package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.session.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "views/login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm) {
//        return "views/login/loginForm";
        SessionMember sessionMember = new SessionMember(loginForm);
        return "redirect:/views/room/roomListForm";
    }

    @GetMapping("/loginInfo")
    public String loginInfo() {
        return "views/login/loginInfoForm";
    }
}
