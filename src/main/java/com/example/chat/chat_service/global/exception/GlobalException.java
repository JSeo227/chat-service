package com.example.chat.chat_service.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalException {

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handle404(NoHandlerFoundException ex, Model model) {
//        model.addAttribute("msg", "요청한 URL을 이 서버에서 찾을 수 없습니다.");
//        return "layout/error";
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handle500(Exception ex, Model model) {
//        model.addAttribute("msg", "서버 내부 오류가 발생하였습니다.");
//        return "layout/error";
//    }
}
