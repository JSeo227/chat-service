package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.MemberRepository;
import com.example.chat.chat_service.session.MemberSession;
import com.example.chat.chat_service.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member) {
        //세션 가져오기
        MemberSession session = SessionManager.getMemberSession();

        //중복 ID 조회
        validateDuplicateMember(member); //유효성 검사

        //loginId, password 등록

        //회원 정보 등록
        memberRepository.save(member);
        return member.getId();
    }


    private void validateDuplicateMember(Member member) {
    }

    private void validateMember(Member member) {
        if (memberRepository.findById(member.getId()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional
    public Member save(Member member) {
        //DB 저장
        return memberRepository.save(member);
    }

    @Transactional
    public void update(Member member) {

        //세션 변경

        //DB 업데이트

    }

    /**
     * 회원 단일 조회
     * @param id
     * @return
     */
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    /**
     * 회원 전체 조회
     * @return
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
