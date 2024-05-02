package com.eminyidle.place.socket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class PlaceInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            // UserId, Username, UserNickname을 헤더에 붙여 줌
            String userId = accessor.getFirstNativeHeader("UserId");
            String userName = accessor.getFirstNativeHeader("Username");
            String userNickname = accessor.getFirstNativeHeader("UserNickname");

            log.info("userNickname: " + userNickname);

            setValue(accessor, "userId", userId);
            setValue(accessor, "userName", userName);
            setValue(accessor, "userNickname", userNickname);
        }
        return message;
    }

    private void setValue(StompHeaderAccessor accessor, String key, Object value) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        sessionAttributes.put(key, value);
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        return sessionAttributes;
    }
}
