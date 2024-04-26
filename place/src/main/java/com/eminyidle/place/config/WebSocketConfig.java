package com.eminyidle.place.config;

import com.eminyidle.place.socket.interceptor.AuthHandshakeInterceptor;
import com.eminyidle.place.socket.interceptor.PlaceActivityInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthHandshakeInterceptor authHandshakeInterceptor;
    private final PlaceActivityInterceptor placeActivityInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic 으로 시작하는 메시지가 메시지 브로커로 라우팅되어야 함
        config.enableSimpleBroker("/topic");
        // /app 으로 시작하는 경로를 가진 메시지를 @MessageMapping 으로 라우팅
        config.setApplicationDestinationPrefixes("/app");
    }

    /*
     * websocket endpoint 등록
     * 브로커 주소의 주소
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/place")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }

    // 클라이언트로부터 들어오는 메시지를 처리할 인터셉터 설정
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(placeActivityInterceptor);
    }
}
