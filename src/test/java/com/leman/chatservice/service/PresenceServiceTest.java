package com.leman.chatservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class PresenceServiceTest {

    private static final Long USER_ID = 1L;
    private static final String KEY = "user:presence:" + USER_ID;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private PresenceService presenceService;

    @Test
    void setOnline_ShouldStore_OnlineStatus() {
        given(redisTemplate.opsForHash()).willReturn(hashOperations);

        presenceService.setOnline(USER_ID);

        then(hashOperations).should(times(1)).put(KEY, "online", "true");
        then(redisTemplate).should(times(1)).expire(KEY, Duration.ofMinutes(5));
    }

    @Test
    void setOffline_ShouldStore_OfflineStatusAndLastSeen() {
        given(redisTemplate.opsForHash()).willReturn(hashOperations);

        presenceService.setOffline(USER_ID);

        then(hashOperations).should(times(1)).put(eq(KEY), eq("online"), eq("false"));
        then(hashOperations).should(times(1)).put(eq(KEY), eq("lastSeen"), anyString());
        then(redisTemplate).should(times(1)).expire(KEY, Duration.ofDays(30));
    }

    @Test
    void isOnline_ShouldReturn_True_WhenUserIsOnline() {
        given(redisTemplate.opsForHash()).willReturn(hashOperations);
        given(hashOperations.get(KEY, "online")).willReturn("true");

        assertTrue(presenceService.isOnline(USER_ID));
    }

    @Test
    void isOnline_ShouldReturn_False_WhenUserIsOffline() {
        given(redisTemplate.opsForHash()).willReturn(hashOperations);
        given(hashOperations.get(KEY, "online")).willReturn("false");

        assertFalse(presenceService.isOnline(USER_ID));
    }

    @Test
    void isOnline_ShouldReturn_False_WhenKeyDoesNotExist() {
        given(redisTemplate.opsForHash()).willReturn(hashOperations);
        given(hashOperations.get(KEY, "online")).willReturn(null);

        assertFalse(presenceService.isOnline(USER_ID));
    }

    @Test
    void getLastSeen_ShouldReturn_Instant_WhenValueExists() {
        Instant now = Instant.now();
        given(redisTemplate.opsForHash()).willReturn(hashOperations);
        given(hashOperations.get(KEY, "lastSeen")).willReturn(now.toString());

        Instant result = presenceService.getLastSeen(USER_ID);

        assertEquals(now, result);
    }

    @Test
    void getLastSeen_ShouldReturn_Null_WhenValueDoesNotExist() {
        given(redisTemplate.opsForHash()).willReturn(hashOperations);
        given(hashOperations.get(KEY, "lastSeen")).willReturn(null);

        assertNull(presenceService.getLastSeen(USER_ID));
    }

    @Test
    void refreshOnline_ShouldReset_TTL() {
        presenceService.refreshOnline(USER_ID);

        then(redisTemplate).should(times(1)).expire(KEY, Duration.ofMinutes(5));
    }

}
