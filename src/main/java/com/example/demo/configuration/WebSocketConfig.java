package com.example.demo.configuration;

import com.example.demo.websocket.DataHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket") // Endpoint WebSocket
                .setAllowedOrigins("*");// Cho phép tất cả nguồn gốc (thay "*" bằng domain cụ thể trong sản xuất)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Nơi gửi tin nhắn đến tất cả client
        config.setApplicationDestinationPrefixes("/app"); // Prefix cho các endpoint server xử lý
    }

}
