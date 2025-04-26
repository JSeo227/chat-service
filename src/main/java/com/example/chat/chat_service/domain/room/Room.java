package com.example.chat.chat_service.domain.room;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "room_type")
@Data
@NoArgsConstructor
public abstract class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name")
    private String name;

    private String password;

    private Integer count;

    private Integer max;

    @Enumerated(EnumType.STRING)
    private RoomType type; //TXT(일반채팅), VID(화상채팅)

}
