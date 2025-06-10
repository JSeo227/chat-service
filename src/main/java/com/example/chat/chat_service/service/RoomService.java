package com.example.chat.chat_service.service;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.domain.room.RoomType;
import com.example.chat.chat_service.domain.room.TextRoom;
import com.example.chat.chat_service.domain.room.VideoRoom;
import com.example.chat.chat_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * 채팅방 생성
     *
     * @param room
     */
    @Transactional
    public void createRoom(Room room) {
        Room newRoom = null;

        if (room.getType() == RoomType.TXT)
            newRoom = TextRoom.createTextRoom(room.getName(), room.getPassword(), room.getMax());
        else if (room.getType() == RoomType.VID)
            newRoom = VideoRoom.createVideoRoom(room.getName(), room.getPassword());

        roomRepository.save(newRoom);
    }

    /**
     * 채팅방 수정
     * @param room
     */
    @Transactional
    public void updateRoom(Room room) {
        Room existingRoom = roomRepository.findById(room.getId())
                .orElseThrow(() -> new IllegalStateException("채팅방이 존재하지 않습니다."));

        existingRoom.setName(room.getName());
        existingRoom.setPassword(room.getPassword());
        existingRoom.setMax(room.getMax());
    }

    /**
     * 채팅방 조회
     * @param id
     * @return
     */
    public Room findRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    /**
     * 전체 채팅방 조회
     * @return
     */
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    /**
     * 채팅방 회원 리스트 조회
     * @param room
     * @return
     */
    public List<Member> getMembersByRoom(Room room) {
        return room.getMembers();
    }

    /**
     * 채팅방에 회원 추가
     * @param roomId
     * @param memberRoom
     */
    @Transactional
    public void enterRoom(Long roomId, MemberRoom memberRoom) {
        // 중복 입장 체크 추가
        if (!isMemberInRoom(roomId, memberRoom.getMember().getId())) {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 방"));
            log.info("enterRoom = {}", room);
            log.info("enterMemberRoom = {}", memberRoom);
            room.addMember(memberRoom);
        }
    }

    /**
     * 채팅방에 회원 삭제
     * @param roomId
     * @param member
     */
    @Transactional
    public void exitRoom(Long roomId, Member member) {
        Room room = roomRepository.findById(roomId).orElse(null);
        room.removeMember(member);
    }

    /**
     * 채팅방 비밀번호 확인
     * @param roomId
     * @param password
     * @return
     */
    public boolean isPasswordValid(Long roomId, String password) {
        Room room = roomRepository.findById(roomId).orElse(null);
        return room.isPasswordValid(password);
    }

    /**
     * 채팅방 중복 여부 확인
     * @param roomId
     * @param memberId
     * @return
     */
    public boolean isMemberInRoom(Long roomId, Long memberId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) return false;
        return room.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(memberId));
    }

}
