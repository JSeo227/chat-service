package com.example.chat.chat_service.global.advice;

import com.example.chat.chat_service.global.exception.OthersException;
import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    // 404 Not Found
    @ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404Exception(Exception ex, Model model) {
        log.warn("404 error occurred: {}", ex.getMessage());
        if (isSessionPresent()) {
            model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("errorMessage", "요청한 URL을 이 서버에서 찾을 수 없습니다.");
            model.addAttribute("detailMessage", ex.getMessage());
            return "layout/error";
        }
        return "/";
    }

    // 500 Internal Server Error
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle500Exception(Exception ex, Model model) {
        log.error("500 error occurred: {}", ex.getMessage());
        if (isSessionPresent()) {
            model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("errorMessage", "서버 내부 오류가 발생하였습니다.");
            model.addAttribute("detailMessage", ex.getMessage());
            return "layout/error";
        }
        return "/";
    }

    // Others Error
    @ExceptionHandler(OthersException.class)
    public String handleOthersException(OthersException ex, Model model) {
        log.warn("others error occurred: {}", ex.getMessage());
        if (isSessionPresent()) {
            model.addAttribute("errorCode", ex.getStatusCode());
            model.addAttribute("errorMessage", "알 수 없는 오류가 발생하였습니다.");
            model.addAttribute("detailMessage", ex.getMessage());
            return "layout/error";
        }
        return "/";
    }

    private static Boolean isSessionPresent() {
        MemberSession memberSession = SessionManager.getMemberSession();
        return memberSession != null;
    }
}
