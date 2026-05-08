package com.leman.chatservice.service;

import static com.leman.chatservice.constant.ApplicationConstant.Common.EMAIL;
import static com.leman.chatservice.constant.ApplicationConstant.Common.ID;
import static com.leman.chatservice.constant.ApplicationConstant.Common.USERNAME;
import static com.leman.chatservice.constant.ApplicationConstant.TokenType;

import com.leman.chatservice.dto.request.LoginRequest;
import com.leman.chatservice.dto.request.RegisterRequest;
import com.leman.chatservice.dto.response.LoginResponse;
import com.leman.chatservice.dto.response.UserResponse;
import com.leman.chatservice.entity.User;
import com.leman.chatservice.exception.DuplicateResourceException;
import com.leman.chatservice.exception.ResourceNotFoundException;
import com.leman.chatservice.exception.UnauthorizedException;
import com.leman.chatservice.exception.constant.ErrorMessage;
import com.leman.chatservice.mapper.TokenMapper;
import com.leman.chatservice.mapper.UserMapper;
import com.leman.chatservice.repository.UserRepository;
import com.leman.chatservice.security.JwtService;
import com.leman.chatservice.security.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String ENTITY = "User";

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        validateUniqueUsername(request.getUsername());
        validateUniqueEmail(request.getEmail());

        User user = createUserFromRequest(request);
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = findUserByUsername(authentication);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("User logged in successfully with ID: {}", user.getId());

        return tokenMapper.toLoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        validateRefreshTokenType(refreshToken);

        Claims claims = jwtService.extractAndValidateClaims(refreshToken);
        Long userId = Long.valueOf(claims.getSubject());

        User user = findExistingUser(userId);
        String newAccessToken = jwtService.generateAccessToken(user);
        log.info("Access token refreshed for user ID: {}", userId);

        return tokenMapper.toLoginResponse(newAccessToken, refreshToken);
    }

    public void logout(String token) {
        String jti = jwtService.getJtiFromToken(token);
        Instant expiration = jwtService.getExpirationInstant(token);
        Duration ttl = Duration.between(Instant.now(), expiration);

        tokenBlacklistService.blacklist(jti, ttl.toMillis());
        log.info("User logged out successfully");
    }

    private User findExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, ID, userId));
    }

    private User findUserByUsername(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, USERNAME, authentication.getName()));
    }

    private User createUserFromRequest(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return user;
    }

    private void validateUniqueUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw DuplicateResourceException.of(ENTITY, USERNAME, username);
        }
    }

    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw DuplicateResourceException.of(ENTITY, EMAIL, email);
        }
    }

    private void validateRefreshTokenType(String token) {
        try {
            Claims claims = jwtService.extractAndValidateClaims(token);
            String tokenType = claims.get("type", String.class);
            if (!TokenType.REFRESH.equals(tokenType)) {
                throw UnauthorizedException.of(ErrorMessage.TOKEN_MALFORMED);
            }
        } catch (JwtException e) {
            throw UnauthorizedException.of(ErrorMessage.INVALID_REFRESH_TOKEN);
        }
    }

}
