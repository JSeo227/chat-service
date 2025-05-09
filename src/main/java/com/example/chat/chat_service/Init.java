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
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    public static class InitService {
        private final EntityManager em;

        public void init() {
            // 회원 생성 및 저장
            Member member1 = createMember1("홍길동", RoleType.MEMBER);
            em.persist(member1);

            Member member2 = createMember2("신짱구", RoleType.MEMBER);
            em.persist(member2);

            // 채팅방 1 생성 및 저장
            TextRoom room1 = createRoom("방1", "1234", 10);
            em.persist(room1);

            // 채팅방 2 생성 및 저장
            TextRoom room2 = createRoom("방2", "5678", 10);
            em.persist(room2);

//            // 회원이 방1에 참여
//            MemberRoom memberRoom1 = MemberRoom.createMemberRoom(member1, room1);
//            room1.addMember(memberRoom1); // 양방향 연관관계 메서드 호출
//            em.persist(memberRoom1);
//
//            MemberRoom memberRoom2 = MemberRoom.createMemberRoom(member2, room1);
//            room1.addMember(memberRoom2); // 양방향 연관관계 메서드 호출
//            em.persist(memberRoom2);
//
//            // 회원이 방2에 참여
//            MemberRoom memberRoom3 = MemberRoom.createMemberRoom(member1, room2);
//            room2.addMember(memberRoom3); // 양방향 연관관계 메서드 호출
//            em.persist(memberRoom3);
        }

        private Member createMember1(String name, RoleType role) {
            Login login = Login.createLogin("m1", "pw1", false);
            return Member.createMember(login, name, role);
        }

        private Member createMember2(String name, RoleType role) {
            Login login = Login.createLogin("m2", "pw2", false);
            return Member.createMember(login, name, role);
        }

        private TextRoom createRoom(String name, String password, Integer max) {
            return (TextRoom) TextRoom.createTextRoom(name, password, max);
        }
    }


}
