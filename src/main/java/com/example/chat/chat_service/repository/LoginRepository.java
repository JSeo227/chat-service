package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, String> {
    Optional<Login> findByPassword(String password);
}