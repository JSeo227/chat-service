package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.login.loginId = :loginId")
    Optional<Member> findByLoginId(@Param("loginId") String loginId);
}