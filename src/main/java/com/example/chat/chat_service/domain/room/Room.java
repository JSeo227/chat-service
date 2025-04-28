package com.example.chat.chat_service.domain.room;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private Integer max; //방 최대 인원 수 (일반 채팅만)

    @Enumerated(EnumType.STRING)
    private RoomType type;

    //양방향
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoom> memberRooms = new ArrayList<>();

    @Transient
    private Integer currentMembers;

    //==연관관계 메서드==//
    public void addMemberRoom(MemberRoom memberRoom) {
        memberRooms.add(memberRoom);
        memberRoom.setRoom(this);
    }

    //==생성 메서드==//
    public static Room createRoom(String name, String password, Integer max, RoomType type, MemberRoom... memberRooms) {
        Room room = new TextRoom();
        room.setName(name);
        room.setPassword(password);
        room.setMax(max);
        room.setType(type);
        for (MemberRoom memberRoom : memberRooms) {
            room.addMemberRoom(memberRoom);
        }
        return room;
    }

    //==로직 메서드==//

    //채팅방에 속한 회원 리스트
    public List<Member> getMembers() {
        return memberRooms.stream()
                .map(MemberRoom::getMember)
                .collect(Collectors.toList());
    }

    //채팅방 인원 +1
    public void addMember(MemberRoom memberRoom) {
        if (isFull()) {
            throw new IllegalStateException("채팅방이 가득 찼습니다.");
        }
        memberRooms.add(memberRoom);
        memberRoom.setCount(memberRoom.getCount() + 1);
        memberRoom.setRoom(this);
    }

    //채팅방 인원 -1
    public void removeMember(MemberRoom memberRoom) {
        if (memberRoom.getCount() > 0) {
            memberRooms.remove(memberRoom);
            memberRoom.setCount(memberRoom.getCount() - 1);
            memberRoom.setRoom(null);
        }
    }

    //채팅방 정원 여부
    public boolean isFull() {
        return memberRooms.size() >= getMax();
    }

    //채팅방 비밀번호 조회
    public boolean isPasswordValid(String password) {
        return getPassword().equals(password);
    }

}
