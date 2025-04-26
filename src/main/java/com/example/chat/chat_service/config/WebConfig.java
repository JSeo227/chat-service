package com.example.chat.chat_service.config;

import com.example.chat.chat_service.interceptor.HttpInterceptor;
import com.example.chat.chat_service.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor())
                .order(0)
                .addPathPatterns("/**")
                .excludePathPatterns("/chat/**");

        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/chat/**");
    }
}
