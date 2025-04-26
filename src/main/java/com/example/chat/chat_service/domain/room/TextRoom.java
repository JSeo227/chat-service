package com.example.chat.chat_service.domain.room;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("T")
@Data
@EqualsAndHashCode(callSuper=true)
public class TextRoom extends Room {

    //양방향
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoom> memberRooms = new ArrayList<>();

    //==생성 메서드==//
    public static Room createTextRoom(String name, String password, Integer count, Integer max) {
        Room room = new TextRoom();
        room.setName(name);
        room.setPassword(password);
        room.setCount(count);
        room.setMax(max);
        return room;
    }

    //==연관관계 메서드==//

    //==로직 메서드==//
    //채팅방 인원 +1
    public void addMember(MemberRoom memberRoom) {
        memberRooms.add(memberRoom);
        setCount(getCount() + 1);
        memberRoom.setRoom(this);
    }

    //채팅방 인원 -1
    public void removeMember(MemberRoom memberRoom) {
        memberRooms.remove(memberRoom);
        setCount(getCount() - 1);
        memberRoom.setRoom(null);
    }

    //채팅방 인원 초과
    public boolean isFull() {
        return memberRooms.size() >= getMax();
    }

    //채팅방 비밀번호 조회
    public boolean isPasswordValid(String password) {
        return getPassword().equals(password);
    }
}
