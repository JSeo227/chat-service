package com.example.chat.chat_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "views/home";
    }

/*    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("content", "home :: content");
        return "layout/main";
    }*/
}
