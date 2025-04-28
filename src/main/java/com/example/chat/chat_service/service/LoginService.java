package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.repository.LoginRepository;
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

    @Transactional
    public Login create(Login login) {
        return Login.createLogin(login.getLoginId(), login.getPassword(), true);
    }

    //아이디 조회
    public Boolean isLoginId(String loginId) {
        return loginRepository.findByLoginId(loginId);
    }

    //비밀번호 조회
    public Boolean isPassword(String password) {
        return loginRepository.findByPassword(password);
    }



}
