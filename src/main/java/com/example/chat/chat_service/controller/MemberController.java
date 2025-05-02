package com.example.chat.chat_service.controller;

import com.example.chat.chat_service.controller.dto.MemberForm;
import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.service.MemberService;
import com.example.chat.chat_service.session.MemberSession;
import com.example.chat.chat_service.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(@ModelAttribute("member") MemberForm form) {
        return "views/members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid @ModelAttribute("member") MemberForm form, BindingResult result) {

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

        return "redirect:/login";
    }

    @PutMapping("/members/{id}/edit")
    public String update(@Valid @ModelAttribute("member") MemberForm form, BindingResult result,
                         @PathVariable Long id) {

        if (result.hasErrors()) {
            return "views/members/modifyMemberForm";
        }

        Login login = new Login();
        login.setLoginId(form.getLoginId());
        login.setPassword(form.getPassword());

        Member member = new Member();
        member.setId(id);
        member.setLogin(login);
        member.setName(form.getName());

        memberService.update(member);

        return "views/rooms/roomListForm";
    }

    @GetMapping("/members/{id}/info")
    public String info(HttpServletRequest request, Model model) {
        MemberSession session = SessionManager.getMemberSession(request);
        Member member = memberService.findById(session.getMemberId());
        model.addAttribute("member", member);
        return "views/members/memberInfoForm";
    }

}
