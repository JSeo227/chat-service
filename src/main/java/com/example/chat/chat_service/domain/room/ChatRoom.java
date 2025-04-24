package com.example.chat.chat_service.domain.room;

import com.example.chat.chat_service.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "dtype")
@Data
public abstract class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @NotEmpty
    @Column(name = "room_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotEmpty
    private Member members;

//    private RoomStatus status; //TXT(일반채팅), VID(화상채팅)
}
