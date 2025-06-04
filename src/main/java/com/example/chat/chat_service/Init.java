package com.example.chat.chat_service;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.RoleType;
import com.example.chat.chat_service.domain.room.TextRoom;
import com.example.chat.chat_service.domain.room.VideoRoom;
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
            TextRoom room1 = createTextRoom("방1", "", 10);
            em.persist(room1);

            // 채팅방 2 생성 및 저장
            TextRoom room2 = createTextRoom("방2", "5678", 10);
            em.persist(room2);

            // 채팅방 3 생성 및 저장
            VideoRoom room3 = createVideoRoom("방3", "");
            em.persist(room3);

        }

        private Member createMember1(String name, RoleType role) {
            Login login = Login.createLogin("m1", "pw1", false);
            return Member.createMember(login, name, role);
        }

        private Member createMember2(String name, RoleType role) {
            Login login = Login.createLogin("m2", "pw2", false);
            return Member.createMember(login, name, role);
        }

        private TextRoom createTextRoom(String name, String password, Integer max) {
            return (TextRoom) TextRoom.createTextRoom(name, password, max);
        }

        private VideoRoom createVideoRoom(String name, String password) {
            return (VideoRoom) VideoRoom.createVideoRoom(name, password);
        }
    }


}
