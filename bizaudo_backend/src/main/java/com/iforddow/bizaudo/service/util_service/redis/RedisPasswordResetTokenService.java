package com.iforddow.bizaudo.service.util_service.redis;

import com.iforddow.bizaudo.redis.templates.RedisObjectTemplate;
import com.iforddow.bizaudo.util.BizUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisPasswordResetTokenService {

    private final RedisObjectTemplate redisObjectTemplate;
    private final RedisConnectionFactory redisConnectionFactory;

    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "passwordResetToken:";
    private static final Duration TOKEN_TTL = Duration.ofMinutes(15);

    @PostConstruct
    private void init() {
        this.redisTemplate = redisObjectTemplate.redisTemplate(redisConnectionFactory);
    }

    public String generateAndStoreToken(UUID userId) {

        String token = UUID.randomUUID().toString();

        if(redisTemplate.hasKey(TOKEN_PREFIX + userId.toString())) {
            redisTemplate.delete(TOKEN_PREFIX + userId);
        }

        redisTemplate.opsForValue().set(TOKEN_PREFIX + userId, token, TOKEN_TTL);

        return token;

    }

    public String getToken(UUID userId) {

        Object value = redisTemplate.opsForValue().get(TOKEN_PREFIX + userId);
        return value != null ? value.toString() : null;

    }

    public boolean validToken(UUID userId, String token) {

        if(!redisTemplate.hasKey(TOKEN_PREFIX + userId)) {
            return false;
        }

        if(BizUtils.isNullOrEmpty(getToken(userId))) {
            return false;
        }

        return getToken(userId).equals(token);

    }

}
