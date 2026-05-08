package com.leman.chatservice.security.interceptor;

import com.leman.chatservice.constant.ApplicationConstant;
import com.leman.chatservice.security.JwtService;
import io.jsonwebtoken.Claims;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith(ApplicationConstant.HttpAttribute.AUTHORIZATION_PREFIX)) {
                return message;
            }
            try {
                String jwt = authHeader.substring(ApplicationConstant.HttpAttribute.AUTHORIZATION_PREFIX.length());
                Claims claims = jwtService.extractAndValidateClaims(jwt);
                Long userId = Long.valueOf(claims.getSubject());
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
                if (sessionAttributes != null) {
                    sessionAttributes.put("userId", userId);
                }
            } catch (Exception e) {
                return message;
            }
        }

        return message;
    }

}
