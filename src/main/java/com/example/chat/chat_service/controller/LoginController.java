package com.example.chat.chat_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "views/login/loginForm";
    }

    @GetMapping("/loginInfo")
    public String loginInfo() {
        return "views/login/loginInfoForm";
    }
}
