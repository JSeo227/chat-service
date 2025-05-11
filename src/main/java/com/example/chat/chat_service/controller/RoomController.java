package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.common.Constants;
import com.example.chat.chat_service.controller.dto.RoomForm;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.domain.room.TextRoom;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.service.RoomService;
import com.example.chat.chat_service.session.MemberSession;
import com.example.chat.chat_service.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
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
    public String home(Model model) {
        List<Room> rooms = roomService.findAll();

        rooms.forEach(room -> {
            int currentMembers = room.getMemberRooms().size();
            room.setCurrentMembers(currentMembers);
        });

        model.addAttribute("rooms", rooms);

        return "views/rooms/roomListForm";
    }

    @GetMapping("/room/create")
    public String createRoom(Model model) {
        model.addAttribute("room", new RoomForm());
        return "views/rooms/createRoomForm";
    }

    @PostMapping("/room/create")
    public String create(@ModelAttribute("room") RoomForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "views/rooms/createRoomForm";
        }

        Room room = new TextRoom();
        room.setName(form.getName());
        room.setPassword(form.getPassword());
        room.setMax(form.getMax());

        roomService.createRoom(room);

        return "redirect:/";
    }

    @PutMapping("/room/{id}/edit")
    public String edit(@ModelAttribute("room") RoomForm form, @PathVariable Long id,
                       BindingResult result) {

        if (result.hasErrors()) {
            return "views/rooms/modifyRoomForm";
        }

        Room room = new TextRoom();
        room.setId(id);
        room.setName(form.getName());
        room.setPassword(form.getPassword());

        roomService.updateRoom(room);

        return "redirect:/";
    }

    @GetMapping("/room/{id}")
    public String enter(@PathVariable Long id, HttpServletRequest request, Model model) {
        MemberSession session = SessionManager.getMemberSession(request);
        Member member = memberService.findById(session.getMemberId());

        Room room = roomService.findRoomById(id);
        model.addAttribute("room", room);

        MemberRoom memberRoom = MemberRoom.createForEnter(member, room);
        roomService.enterRoom(id, memberRoom);
        return "views/rooms/roomForm";
    }

    @GetMapping("/room/exit/{id}")
    public String exit(@PathVariable Long id, HttpServletRequest request) {
        MemberSession session = SessionManager.getMemberSession(request);
        Member member = memberService.findById(session.getMemberId());

        roomService.exitRoom(id, member);
        return "redirect:/";
    }

    @PostMapping("/room/check")
    public boolean checkPassword(@ModelAttribute("room") RoomForm form,
                                 @RequestParam Long id, @RequestParam String password) {
        return roomService.isPasswordValid(id, password);
    }
}
