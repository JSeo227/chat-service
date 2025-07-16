package com.example.chat.chat_service.global.interceptor;

import com.example.chat.chat_service.global.common.Constants;
import com.example.chat.chat_service.global.session.MemberSession;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketSessionInterceptor implements HandshakeInterceptor {

    // WebSocket 연결 전
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Http Session -> WebSocket Session
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);
            if (httpSession != null) {
                MemberSession memberSession = (MemberSession) httpSession.getAttribute("memberSession");
                attributes.put(Constants.MEMBER_SESSION.getName(), memberSession);
            }
        }
        return true;
    }

    // WebSocket 연결 후
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
