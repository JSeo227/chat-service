package com.example.chat.chat_service.domain;

import com.example.chat.chat_service.domain.room.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @JsonIgnore
    private LocalDateTime enteredDate; // 입장 시간

    @JsonIgnore
    private LocalDateTime exitedDate; // 퇴장 시간

    @PrePersist
    public void prePersist() {
        enteredDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        exitedDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "MemberRoom{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberRoom that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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

}
