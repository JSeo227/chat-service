package com.example.chat.chat_service;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.RoleType;
import com.example.chat.chat_service.domain.room.TextRoom;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Init {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init1();
//        initService.init2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    public static class InitService {
        private final EntityManager em;

        public void init1() {
            // 회원 생성 및 저장
            Member member = createMember("홍길동", RoleType.MEMBER);
            em.persist(member);

            // 채팅방 1 생성 및 저장
            TextRoom room1 = createRoom("방1", "1234", 0, 10);
            em.persist(room1);

            // 채팅방 2 생성 및 저장
            TextRoom room2 = createRoom("방2", "5678", 0, 10);
            em.persist(room2);

            // 회원이 방1에 참여
            MemberRoom memberRoom1 = MemberRoom.createMemberRoom(member, room1);
            room1.addMember(memberRoom1); // 양방향 연관관계 메서드 호출
            em.persist(memberRoom1);

            // 회원이 방2에 참여
            MemberRoom memberRoom2 = MemberRoom.createMemberRoom(member, room2);
            room2.addMember(memberRoom2); // 양방향 연관관계 메서드 호출
            em.persist(memberRoom2);
        }

        private Member createMember(String name, RoleType role) {
            Login login = Login.createLogin("testUser", "password");
            Member member = Member.createMember(login, name, role);
            return member;
        }

        private TextRoom createRoom(String name, String password, Integer count, Integer max) {
            return (TextRoom) TextRoom.createTextRoom(name, password, count, max);
        }
    }


}
