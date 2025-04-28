package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; //회원 아이디

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login_id")
    private Login login; //로그인 아이디

    private String name; //이름

    @Enumerated(EnumType.STRING) //ADMIN, MEMBER
    private RoleType role; //역할

    private LocalDateTime createdDate; //가입일

    private LocalDateTime updatedDate; //수정일

    // PrePersist와 PreUpdate를 사용하여 날짜를 자동으로 설정
    @PrePersist
    public void prePersist() {
        if (role == null) role = RoleType.MEMBER; // 기본값 설정

        createdDate = LocalDateTime.now();
        updatedDate = createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    //==생성 메서드==//
    public static Member createMember(Login login, String name, RoleType role) {
        Member member = new Member();
        member.setLogin(login);
        member.setName(name);
        member.setRole(role);
        return member;
    }

    //==로직 메서드==//

}
