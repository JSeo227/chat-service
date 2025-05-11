package com.example.chat.chat_service.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Slf4j
@Component
public class HttpInterceptor implements HandlerInterceptor {

    // Controller 호출 전 [ preHandle → Controller ]
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURL = request.getRequestURL().toString();
        String clientIP = request.getHeader("X-FORWARDED-FOR");
        if (clientIP == null) {
            clientIP = request.getRemoteAddr();
        }

        log.debug("[preHandle] Request URL: {}", requestURL);
        log.info("[preHandle] Client IP: {} | Remote IP: {} | URL: {}",
                request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr(), requestURL);

        List<Map<String, String>> headers = new ArrayList<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headers.add(Map.of(name, value));
        }

        log.debug("[preHandle] Header Count: {} | Headers: {}", headers.size(), headers);

        return true;
    }

    // Controller 실행 후, View 실행 전 [Controller -> postHandle]
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[postHandle] Controller 처리 완료: {}", request.getRequestURI());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }

    // View 종료 후 실행 [postHandle -> View]
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("[afterCompletion] 응답 완료 - 인코딩: {}", response.getCharacterEncoding());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
