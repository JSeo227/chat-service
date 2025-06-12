package com.example.chat.chat_service.domain.room;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @Max(99)
    @Min(1)
    private Integer max; //방 최대 인원 수 (일반 채팅만)

    @Enumerated(EnumType.STRING)
    private RoomType type;

    //양방향
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoom> memberRooms = new ArrayList<>();

    @Transient
    private Integer currentMembers; //현재 방 인원 수

    //==연관관계 메서드==//
    public void addMemberRoom(MemberRoom memberRoom) {
        memberRooms.add(memberRoom);
        memberRoom.setRoom(this);
    }

    //==생성 메서드==//
    public static Room createRoom(String name, String password, Integer max, RoomType type, MemberRoom... memberRooms) {
        Room room = switch (type) {
            case TXT -> new TextRoom();
            case VID -> new VideoRoom();
            default -> throw new IllegalArgumentException("지원하지 않는 RoomType: " + type);
        };
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
                .toList();
    }

    //채팅방 인원 +1
    public void addMember(MemberRoom memberRoom) {
        memberRooms.add(memberRoom);
        memberRoom.setRoom(this);
    }

    //채팅방 인원 -1
    public void removeMember(Member member) {
        log.info("exitMemberRooms: {}", memberRooms);
        log.info("exitMember: {}", member);

        MemberRoom memberRoom = memberRooms.stream()
                .filter(mr -> mr.getMember().equals(member))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("회원이 이 채팅방에 없습니다."));

        memberRoom.exit();
//        memberRoom.setIsInRoom(false); // 소프트 삭제 <- 이거 고민중
        memberRooms.remove(memberRoom); // 하드 삭제
    }

    //채팅방 비밀번호 조회
    public boolean isPasswordValid(String password) {
        return getPassword().equals(password);
    }

}
