package com.example.chat.chat_service.global.session;

import com.example.chat.chat_service.global.common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
     * [HttpServletRequest 공용 메소드]
     * HttpServletRequest를 RequestContextHolder를 통해 전역적으로 접근
     * @return
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No request bound to current thread");
        }
        return attributes.getRequest();
    }

    /**
     * 회원 세션 조회
     * @return
     */
    public static MemberSession getMemberSession() {
        HttpSession session = getRequest().getSession(false);
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
     * @param memberSession
     */
    public static void setMemberSession(MemberSession memberSession) {
        HttpSession session = getRequest().getSession(true);
        session.setAttribute(Constants.MEMBER_SESSION, memberSession);
    }

    /**
     * 회원 세션 삭제
     * @param memberSession
     */
    public static void removeMemberSession(MemberSession memberSession) {
        HttpSession session = getRequest().getSession(false);
        if (session != null) {
            session.setAttribute(Constants.MEMBER_SESSION, memberSession);
            session.invalidate();
        }
    }
}
