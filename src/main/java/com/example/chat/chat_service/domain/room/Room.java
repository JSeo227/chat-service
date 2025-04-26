package com.example.chat.chat_service.domain.room;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "room_type")
@Data
public abstract class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id; //채팅방 아이디

    private String name; //이름

    private String password; //비밀번호

    private Integer count; //현재 방 인원 수

    private Integer max; //방 최대 인원 수

}
