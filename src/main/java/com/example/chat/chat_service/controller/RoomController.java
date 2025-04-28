package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.common.Constants;
import com.example.chat.chat_service.controller.dto.RoomForm;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.domain.MemberRoom;
import com.example.chat.chat_service.domain.room.Room;
import com.example.chat.chat_service.domain.room.TextRoom;
import com.example.chat.chat_service.service.RoomService;
import com.example.chat.chat_service.session.MemberSession;
import com.example.chat.chat_service.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
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
        return "views/members/createMemberForm";
    }

    @PostMapping("/room/create")
    public String create(@ModelAttribute RoomForm form, BindingResult result) {

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
    public String edit(@ModelAttribute RoomForm form, @PathVariable Long id,
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
    public String enter(@PathVariable Long id, HttpServletRequest request, @SessionAttribute(Constants.MEMBER_SESSION) Member member) {
        Room room = roomService.findRoomById(id);
        MemberSession session = SessionManager.getMemberSession(request);
        log.info(room.toString());
        log.info(session.toString());
        log.info(member.toString());
        MemberRoom memberRoom = MemberRoom.createMemberRoom(member, room);
        roomService.enterRoom(id, memberRoom);
        return "views/rooms/roomForm";
    }

    @PostMapping("/room/{id}/{password}")
    public boolean checkPassword(@ModelAttribute RoomForm form,
                                 @PathVariable Long id, @PathVariable String password) {
        return roomService.isPasswordValid(id, password);
    }
}
