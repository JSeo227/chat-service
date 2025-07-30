package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.controller.dto.CheckPasswordDto;
import com.example.chat.chat_service.controller.dto.MemberDto;
import com.example.chat.chat_service.controller.dto.RoomDto;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.domain.room.TextRoom;
import com.example.chat.chat_service.controller.dto.SessionDto;
import com.example.chat.chat_service.global.common.ResponseApi;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.service.RoomService;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.chat.chat_service.global.common.ResponseApi.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;
    private final MemberService memberService;

    @GetMapping("/loader")
    ResponseEntity<ResponseApi<List<RoomDto>>> rooms() {
        List<Room> rooms = roomService.findAll();

        rooms.forEach(room -> {
            int currentMembers = room.getMemberRooms().size();
            room.setCurrentMembers(currentMembers);
        });

        List<RoomDto> roomDtos = rooms.stream()
                .map(room -> RoomDto.builder()
                        .id(room.getId())
                        .name(room.getName())
                        .password(room.getPassword())
                        .max(room.getMax())
                        .currentMembers(room.getCurrentMembers())
                        .type(room.getType())
                        .build())
                .toList();

        return ResponseEntity.ok(success(roomDtos));
    }

    @GetMapping("/session")
    public ResponseEntity<ResponseApi<SessionDto>> session() {
        MemberSession memberSession = SessionManager.getMemberSession();

        SessionDto session = SessionDto.builder()
                .memberId(memberSession.getMemberId())
                .loginId(memberSession.getLoginId())
                .checked(memberSession.getChecked())
                .name(memberSession.getName())
                .build();

        return ResponseEntity.ok(success(session));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseApi<String>> create(@Valid @RequestBody RoomDto request) {
        Room room = new TextRoom();
        room.setName(request.getName());
        room.setPassword(request.getPassword());
        room.setMax(request.getMax());
        room.setType(request.getType());

        roomService.createRoom(room);

        return ResponseEntity.ok(success("OK!"));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseApi<String>> delete(@PathVariable("id") Long id) {
        Room room = roomService.findRoomById(id);

        if(room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("방을 찾을 수 없습니다."));
        }

        List<Member> members = room.getMembers();
        for (Member member : members) {
            roomService.deleteRoom(room, member);
        }

        return ResponseEntity.ok(success("OK!"));
    }

    @PostMapping("/{id}/enter")
    public ResponseEntity<ResponseApi<RoomDto>> enter(@PathVariable("id") Long id) {
        MemberSession session = SessionManager.getMemberSession();

        Member member = memberService.findById(session.getMemberId());
        Room room = roomService.findRoomById(id);

        RoomDto roomDto = RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .password(room.getPassword())
                .max(room.getMax())
                .currentMembers(room.getCurrentMembers())
                .type(room.getType())
                .build();

        MemberRoom memberRoom = MemberRoom.createForEnter(member, room);
        roomService.enterRoom(id, memberRoom);

        return ResponseEntity.ok(success(roomDto));
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<ResponseApi<String>> exit(@PathVariable("id") Long id) {
        MemberSession session = SessionManager.getMemberSession();

        Room room = roomService.findRoomById(id);
        Member member = memberService.findById(session.getMemberId());

        roomService.exitRoom(room, member);
        return ResponseEntity.ok(success("OK!"));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ResponseApi<List<String>>> members(@PathVariable("id") Long id) {
        Room room = roomService.findRoomById(id);
        List<MemberDto> result = roomService.getMembersByRoom(room).stream()
                .map(MemberDto::new)
                .toList();
        List<String> names = result.stream()
                .map(MemberDto::getName)
                .toList();
        return ResponseEntity.ok(success(names));
    }

    @PostMapping("/check-password")
    public ResponseEntity<ResponseApi<String>> checkPassword(
            @RequestBody CheckPasswordDto request
    ) {
        roomService.isPasswordValid(request.getRoomId(), request.getPassword());
        return ResponseEntity.ok(success("OK!"));
    }

}