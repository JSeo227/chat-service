package com.example.chat.chat_service.session;

import com.example.chat.chat_service.common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionManager implements HttpSessionListener {

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
        sessionStore.remove(se.getSession().getId());
        log.info("세션 삭제됨: {}", se.getSession().getId());
    }

    /**
     * 회원 세션 조회
     * @param request
     * @return
     */
    public static MemberSession getMemberSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("session is null");
        } else {
            MemberSession memberSession = (MemberSession) session.getAttribute(Constants.MEMBER_SESSION);
            if (memberSession == null) {
                throw new RuntimeException("memberSession is null");
            }
            return memberSession;
        }
    }

    /**
     * 회원 세션 저장
     * @param request
     * @param memberSession
     */
    public static void setMemberSession(HttpServletRequest request, MemberSession memberSession) {
        HttpSession session = request.getSession(true);
        session.setAttribute(Constants.MEMBER_SESSION, memberSession);
    }

    /**
     * 회원 세션 삭제
     * @param request
     * @param memberSession
     */
    public static void removeMemberSession(HttpServletRequest request, MemberSession memberSession) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(Constants.MEMBER_SESSION, memberSession);
            session.invalidate();
        }
    }
}
