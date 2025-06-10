package com.example.chat.chat_service.domain;

import com.example.chat.chat_service.domain.room.Room;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class MemberRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_room_id")
    private Long id; //회원방 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //회원 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room; //방 아이디

    private Boolean isInRoom; // 방에 있는가

    private LocalDateTime enteredDate; // 입장 시간

    private LocalDateTime exitedDate; // 퇴장 시간

    @PrePersist
    public void prePersist() {
        enteredDate = LocalDateTime.now();
    }

    public void exit() {
        this.exitedDate = LocalDateTime.now();
    }

    //==생성 메서드==//
    public static MemberRoom createForEnter(Member member, Room room) {
        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setMember(member);
        memberRoom.setRoom(room);
        memberRoom.setIsInRoom(true);
        return memberRoom;
    }

    //==로직 메서드==//

}
