package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private MemberRepository memberRepository;

    //로그인 아이디 조회
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    //로그인 비밀번호 조회

    //로그인 오듀 횟수 등록

    //로그인 오류 횟수 조회

    //로그인 오류 횟수 초기화
}
