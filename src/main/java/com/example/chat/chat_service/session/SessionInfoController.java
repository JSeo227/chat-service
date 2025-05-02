package com.example.chat.chat_service.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false); //null
        if (session == null) {
            return "세션이 없습니다.";
        }

        //세션 데이터 출력
        StringBuilder sb = new StringBuilder();
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> {
                    Object value = session.getAttribute(name);
                    sb.append(name).append(" : ").append(value).append("\n");
                });

        return sb.toString();

    }
}
