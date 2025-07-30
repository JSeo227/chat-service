package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.controller.dto.MemberDto;
import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.global.common.ResponseApi;
import com.example.chat.chat_service.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.chat.chat_service.global.common.ResponseApi.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<ResponseApi<String>> join(@RequestBody MemberDto request) {

        log.info("join request {}", request);

        Login login = new Login();
        login.setLoginId(request.getLoginId());
        login.setPassword(request.getPassword());

        Member member = new Member();
        member.setName(request.getName());
        member.setLogin(login);

        memberService.join(member);

        return ResponseEntity.ok(success("OK!"));
    }

}