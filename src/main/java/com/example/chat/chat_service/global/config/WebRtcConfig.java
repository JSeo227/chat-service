package com.example.chat.chat_service.global.config;

import com.example.chat.chat_service.global.interceptor.WebSocketSessionInterceptor;
import com.example.chat.chat_service.global.websocket.SignalHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Profile("!test")
public class WebRtcConfig implements WebSocketConfigurer {

    private final SignalHandler signalHandler;

    // signal 로 요청이 왔을 때 아래의 WebSockerHandler 가 동작하도록 registry 에 설정
    // 요청은 클라이언트 접속, close, 메시지 발송 등에 대해 특정 메서드를 호출한다
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalHandler, "/signal")
                .setAllowedOrigins("*")
                // WebSocketSession에 HttpSession의 attribute 복사
                .addInterceptors(new WebSocketSessionInterceptor());
    }

    @Bean
    public ServletServerContainerFactoryBean createWerSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192); // 텍스트 메시지 최대 크기를 8KB로 설정
        container.setMaxBinaryMessageBufferSize(8192); // 바이너리 메시지 최대 크기를 8KB로 설정
        return container;
    }
}
