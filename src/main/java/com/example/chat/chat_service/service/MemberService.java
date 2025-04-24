package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //아이디 중복 검증
    private void validateDuplicateMember(Member member) {
        String loginId = member.getLoginId();

    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
