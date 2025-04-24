package com.example.chat.chat_service.interceptor;

import com.example.chat.chat_service.session.MemberSession;
import com.example.chat.chat_service.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    // Controller 호출 전 [ preHandle → Controller ]
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        MemberSession memberSession = SessionManager.getMemberSession(request);

        if (memberSession == null) {
            log.info("MemberSession is null");
            Map<String, Object> param = new HashMap<>();

            String url = request.getRequestURI(); // 요청URL
            String queryStr = request.getQueryString();
            String preUrl = "";

            if(url == null) {
                preUrl = url;
            } else {
                preUrl = (queryStr == null) ? url : url+"?"+queryStr;
            }

            // 직전 URL을 세션에 저장
            param.put("preUrl", preUrl);
//            SessionManager.setSession(request, param);

            //세션이 없으면 -> 뒤로가기 방지
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            //세션이 없으면 -> 로그인 화면으로 이동
            response.sendRedirect("/login/loginForm");
            return false;
        }

        return true;
    }

    // Controller 실행 후, View 실행 전 [Controller -> postHandle]
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle {}", modelAndView);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // View 종료 후 실행 [postHandle -> View]
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
