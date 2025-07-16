package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.LoginRepository;
import com.example.chat.chat_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param login
     * @return
     */
    @Transactional
    public Login create(Login login) {
        return Login.createLogin(login.getLoginId(), login.getPassword(), true);
    }


    /**
     * 로그인 성공 유무
     * @param loginId
     * @param status
     */
    @Transactional
    public void setLoginStatusTrue(String loginId, boolean status) {
        Member member = memberRepository.findByLoginId(loginId).orElse(null);
        if (member != null) {
            member.getLogin().setIsLogin(status);
        }
    }

    /**
     * 아이디 조회
     * @param loginId
     * @return
     */
    public Boolean isLoginId(String loginId) {
        return loginRepository.findById(loginId).isPresent();
    }

    /**
     * 비밀번호 조회
     * @param password
     * @return
     */
    public Boolean isPassword(String password) {
        return loginRepository.findByPassword(password).isPresent();
    }

}
