package com.leman.chatservice.service;

import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PresenceService {

    private static final String PRESENCE_KEY_PREFIX = "user:presence:";
    private static final Duration ONLINE_TTL = Duration.ofMinutes(5);
    private static final Duration OFFLINE_TTL = Duration.ofDays(30);

    private final RedisTemplate<String, String> redisTemplate;

    public void setOnline(Long userId) {
        String key = PRESENCE_KEY_PREFIX + userId;
        redisTemplate.opsForHash().put(key, "online", "true");
        redisTemplate.expire(key, ONLINE_TTL);
    }

    public void setOffline(Long userId) {
        String key = PRESENCE_KEY_PREFIX + userId;
        redisTemplate.opsForHash().put(key, "online", "false");
        redisTemplate.opsForHash().put(key, "lastSeen", Instant.now().toString());
        redisTemplate.expire(key, OFFLINE_TTL);
    }

    public boolean isOnline(Long userId) {
        String value = (String) redisTemplate.opsForHash().get(PRESENCE_KEY_PREFIX + userId, "online");
        return "true".equals(value);
    }

    public Instant getLastSeen(Long userId) {
        String value = (String) redisTemplate.opsForHash().get(PRESENCE_KEY_PREFIX + userId, "lastSeen");
        return value != null ? Instant.parse(value) : null;
    }

    public void refreshOnline(Long userId) {
        String key = PRESENCE_KEY_PREFIX + userId;
        redisTemplate.expire(key, ONLINE_TTL);
    }

}
