package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("member", new MemberForm());
        return "views/members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid @ModelAttribute MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "views/members/createMemberForm";
        }

        Login login = new Login();
        login.setLoginId(form.getLoginId());
        login.setPassword(form.getPassword());

        Member member = new Member();
        member.setName(form.getName());
        member.setLogin(login);

        memberService.join(member);

//        return "redirect:/views/rooms/roomListForm";
        return "views/rooms/roomListForm";
    }

}
