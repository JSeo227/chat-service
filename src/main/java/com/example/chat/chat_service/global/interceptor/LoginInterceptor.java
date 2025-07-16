package com.example.chat.chat_service.global.interceptor;

import com.example.chat.chat_service.global.session.MemberSession;
import com.example.chat.chat_service.global.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    // Controller 호출 전 [ preHandle → Controller ]
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            MemberSession memberSession = SessionManager.getMemberSession();
            log.info("MemberSession is NOT null:{}", memberSession);
        } catch (Exception e) { //세션이 없으면
            log.info("MemberSession is null");

            // 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            // 뒤로가기 방지
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            // 로그인 화면으로 이동
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    // Controller 실행 후, View 실행 전 [Controller -> postHandle]
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[postHandle] Url: {}", request.getRequestURI());
        log.info("[postHandle] Status: {}", response.getStatus());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // View 종료 후 실행 [postHandle -> View]
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
