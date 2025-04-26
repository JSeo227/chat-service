package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
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

    //양방향
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Friend> friends = new ArrayList<>();

    // PrePersist와 PreUpdate를 사용하여 날짜를 자동으로 설정
    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        updatedDate = createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public static Member createMember(Login login, String name, RoleType role) {
        Member member = new Member();
        member.login = login;
        member.name = name;
        member.role = role;
        return member;
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
        friend.setMember(this);
    }

}
