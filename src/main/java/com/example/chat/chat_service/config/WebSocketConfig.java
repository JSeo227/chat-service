package com.example.chat.chat_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // 클라이언트가 연결할 WebSocket 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // 클라이언트가 연결할 주소
                .setAllowedOriginPatterns("*") // CORS 허용
                .withSockJS(); // SockJS fallback 옵션
    }

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독(수신)하는 요청 -> 메시지 받을 때
        registry.enableSimpleBroker("/topic", "/queue"); // 구독 주소

        // 메시지를 발행(송신)하는 요청 -> 메시지 보낼 때
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지 보낼 주소
    }
}
