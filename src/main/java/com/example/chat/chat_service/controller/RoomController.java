package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.controller.dto.MemberDto;
import com.example.chat.chat_service.controller.dto.RoomDto;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.domain.room.RoomType;
import com.example.chat.chat_service.domain.room.TextRoom;
import com.example.chat.chat_service.controller.dto.SessionDto;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.service.RoomService;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final MemberService memberService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "memberSession", required = false) MemberSession memberSession, Model model) {
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

        SessionDto session = SessionDto.builder()
                .memberId(memberSession.getMemberId())
                .loginId(memberSession.getLoginId())
                .checked(memberSession.getChecked())
                .name(memberSession.getName())
                .build();

        model.addAttribute("rooms", roomDtos);
        model.addAttribute("memberSession", session);

        return "views/rooms/roomListForm";
    }

    @GetMapping("/room/create")
    public String createRoom(Model model) {
        model.addAttribute("room", RoomDto.builder().build());
        return "views/rooms/createRoomForm";
    }

    @PostMapping("/room/create")
    public String create(@Valid @ModelAttribute("room") RoomDto form, BindingResult result) {

        log.info("Create room: {}", form);
        log.info("Result : {}", result);

        if (result.hasErrors()) {
            return "views/rooms/createRoomForm";
        }

        Room room = new TextRoom();
        room.setName(form.getName());
        room.setPassword(form.getPassword());
        room.setMax(form.getMax());
        room.setType(form.getType());

        roomService.createRoom(room);

        return "redirect:/";
    }

    @PostMapping("/room/delete/{id}")
    public String delete(@PathVariable Long id) {
        Room room = roomService.findRoomById(id);
        List<Member> members = room.getMembers();
        for (Member member : members) {
            roomService.deleteRoom(room, member);
        }
        return "redirect:/";
    }

    @GetMapping("/room/{id}")
    public String enter(@PathVariable Long id, Model model) {
        MemberSession session = SessionManager.getMemberSession();
        Member member = memberService.findById(session.getMemberId());

        Room room = roomService.findRoomById(id);

        RoomDto form = RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .password(room.getPassword())
                .max(room.getMax())
                .currentMembers(room.getCurrentMembers())
                .type(room.getType())
                .build();

        model.addAttribute("room", form);

        MemberRoom memberRoom = MemberRoom.createForEnter(member, room);
        roomService.enterRoom(id, memberRoom);

        if (room.getType() == RoomType.TXT)
            return "views/rooms/textRoomForm";
        else if (room.getType() == RoomType.VID)
            return "views/rooms/videoRoomForm";
        else
            return "redirect:/";
    }

    @GetMapping("/room/{id}/list")
    @ResponseBody
    public List<String> membersByRoom(@PathVariable Long id) {
        Room room = roomService.findRoomById(id);
        List<MemberDto> result = roomService.getMembersByRoom(room).stream()
                .map(MemberDto::new)
                .toList();
        List<String> names = result.stream()
                .map(MemberDto::getName)
                .toList();
        return names;
    }

    @PostMapping("/room/exit/{id}")
    public String exit(@PathVariable Long id) {
        Room room = roomService.findRoomById(id);

        MemberSession session = SessionManager.getMemberSession();
        Member member = memberService.findById(session.getMemberId());

        roomService.exitRoom(room, member);
        return "redirect:/";
    }

    @PostMapping("/room/check")
    @ResponseBody
    public Boolean checkPassword(@ModelAttribute("room") RoomDto form,
                                 @RequestParam("id") Long roomId, @RequestParam("password") String password) {
        log.info("checkPassword id = {}, password = {}", roomId, password);
        return roomService.isPasswordValid(roomId, password);
    }

}
