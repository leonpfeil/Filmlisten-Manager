package com.sep.server.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//https://spring.io/guides/gs/messaging-stomp-websocket/
//Boilerplate code von hier
@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.enableSimpleBroker("/group","/private", "/meta");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat").withSockJS(); //withsocksj für webchat
    }
}
