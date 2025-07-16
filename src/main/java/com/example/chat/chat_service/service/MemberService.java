package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.MemberRepository;
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
    public Member join(Member member) {
        // 중복 ID 조회
        validateDuplicateLoginId(member);

        // 로그인 정보 등록
        Login newLogin = Login.createLogin(member.getLogin().getLoginId(), member.getLogin().getPassword(), false);

        // 회원 정보 등록
        Member newMember = Member.createMember(newLogin, member.getName(), null);

        return memberRepository.save(newMember); //persist
    }

    private void validateDuplicateLoginId(Member member) {
        Optional<Member> findLoginId = memberRepository.findByLoginId(member.getLogin().getLoginId());
        if (findLoginId.isPresent()) {
            throw new IllegalStateException("이미 존재하는 로그인 ID 입니다.");
        }

    }

    /**
     * 회원 수정 - dirty checking
     * @param member
     */
    @Transactional
    public void update(Member member) {
        // 회원 정보 수정
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        existingMember.setName(member.getName());

        // 로그인 정보 수정
        Login existingLogin = existingMember.getLogin();

        if (!existingLogin.getLoginId().equals(member.getLogin().getLoginId())) {
            existingLogin.setLoginId(member.getLogin().getLoginId());
        }
        if (!existingLogin.getPassword().equals(member.getLogin().getPassword())) {
            existingLogin.setPassword(member.getLogin().getPassword());
        }
    }

    /**
     * 회원 조회
     * @param id
     * @return
     */
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    /**
     * 회원 조회
     * @param loginId
     * @return
     */
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).orElse(null);
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
