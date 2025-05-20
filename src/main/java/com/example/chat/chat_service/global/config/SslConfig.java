package com.example.chat.chat_service.global.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SslConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        // 기본 https 설정
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        // http로 요청이 들어오면 https로 redirect
        tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector_8080());

        return tomcat;
    }

    /**
     * http 를 https 로 리다이렉트한다.
     * 즉 http://8080 으로 요청이 들어온 경우 리다이렉트를 통해서 https://8443 으로 변경해준다
     */
    private Connector httpToHttpsRedirectConnector_8080() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

}
