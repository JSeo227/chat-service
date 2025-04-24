package com.example.chat.chat_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomController {

    @GetMapping("/")
    public String room() {
        return "views/room/roomListForm";
    }

    // @PathVariable 사용
    @GetMapping("/rooms")
    public String rooms() {
        return "views/room/roomForm";
    }
}
