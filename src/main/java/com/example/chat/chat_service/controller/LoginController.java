package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.controller.dto.JoinDto;
import com.example.chat.chat_service.controller.dto.LoginDto;
import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.global.common.ResponseApi;
import com.example.chat.chat_service.service.LoginService;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.chat.chat_service.global.common.ResponseApi.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ResponseApi<MemberSession>> login(@RequestBody LoginDto request) {

        // 사용자 존재 유무 확인 로직
        Member existingMember = memberService.findByLoginId(request.getLoginId());
        if (existingMember == null) {
            return ResponseEntity.badRequest().body(error("존재하지 않는 회원입니다."));
        }

        // 비밀번호 일치 확인 로직
        Login login = existingMember.getLogin();
        if (!login.getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body(error("비밀번호가 일치하지 않습니다."));
        }

        //세션 저장 로직
        MemberSession memberSession = MemberSession.builder()
                .memberId(existingMember.getId())
                .loginId(request.getLoginId())
                .checked(request.getChecked())
                .name(existingMember.getName())
                .isLogin(true)
                .loginAt(LocalDateTime.now())
                .build();

        log.info("memberSession {}", memberSession);

        SessionManager.setMemberSession(memberSession);

        return ResponseEntity.ok(success(memberSession));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseApi<String>> logout(HttpServletRequest request, HttpServletResponse response) {

        MemberSession memberSession = SessionManager.getMemberSession();
        loginService.setLoginStatusTrue(memberSession.getLoginId(), false);
        SessionManager.removeMemberSession(memberSession);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setHeader("Expires", "0"); // Proxies

        return ResponseEntity.ok(success("OK!"));
    }

}