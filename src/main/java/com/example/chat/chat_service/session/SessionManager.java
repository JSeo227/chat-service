package com.example.chat.chat_service.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionManager implements HttpSessionListener {

    public static final String MEMBER_SESSION = "memberSession";
    private static final Map<String, HttpSession> sessionStore = new ConcurrentHashMap<>();

    /**
     * [세션 시간]
     * 초기값: 1800
     */
    @Value("${session.timeout:1800}")
    private int sessionTime;

    /**
     * [세션 리스너]
     * 세션 생성시 자동 호출
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(sessionTime);
        sessionStore.put(se.getSession().getId(), se.getSession());
        log.info("세션 생성됨: {}", se.getSession().getId());

    }

    /**
     * [세션 리스너]
     * 세션 삭제시 자동 호출
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = sessionStore.remove(se.getSession().getId());
        log.info("세션 삭제됨: {}", se.getSession().getId());
    }

    //세션 조회

    //세션 저장

    //세션 삭제

    /**
     * 사용자 세션 조회
     * @param request
     * @return
     */
    public static MemberSession getMemberSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (MemberSession) session.getAttribute(MEMBER_SESSION);
    }

    /**
     * 사용자 세션 삭제
     * @param request
     * @param memberSession
     */
    public static void removeMemberSession(HttpServletRequest request, MemberSession memberSession) {
        HttpSession session = request.getSession(false);
        session.setAttribute(MEMBER_SESSION, memberSession);
    }
}
