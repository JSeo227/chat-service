package com.example.chat.chat_service.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository {
    Boolean findByLoginId(String loginId);
    Boolean findByPassword(String password);
}
