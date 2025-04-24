package com.example.chat.chat_service.repository;

import com.example.chat.chat_service.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {
    Member save(Member member);
    Member findById(Long id);
    List<Member> findByName(String name);
    List<Member> findAll();
}
