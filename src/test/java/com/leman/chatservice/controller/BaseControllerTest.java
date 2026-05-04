package com.leman.chatservice.controller;

import com.leman.chatservice.config.SecurityConfig;
import com.leman.chatservice.security.CustomUserDetailsService;
import com.leman.chatservice.security.JwtService;
import com.leman.chatservice.security.TokenBlacklistService;
import com.leman.chatservice.security.handler.CustomAccessDeniedHandler;
import com.leman.chatservice.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest
@AutoConfigureJsonTesters
@Import({
        SecurityConfig.class,
        CustomAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class
})
public abstract class BaseControllerTest {

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    protected TokenBlacklistService tokenBlacklistService;

}
