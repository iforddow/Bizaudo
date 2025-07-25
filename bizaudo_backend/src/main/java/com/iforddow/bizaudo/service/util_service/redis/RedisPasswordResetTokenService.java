package com.iforddow.bizaudo.service.util_service.redis;

import com.iforddow.bizaudo.util.BizUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisPasswordResetTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "passwordResetToken:";
    private static final Duration TOKEN_TTL = Duration.ofMinutes(15);

    public UUID generateAndStoreToken(UUID userId) {

        UUID token = UUID.randomUUID();

        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, userId, TOKEN_TTL);

        return token;

    }

    public UUID getTokenValue(UUID token) {

        Object value = redisTemplate.opsForValue().get(TOKEN_PREFIX + token);
        return value != null ? UUID.fromString(value.toString()) : null;

    }

    public void deleteToken(UUID token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
    }

    public boolean validToken(UUID token) {

        return redisTemplate.hasKey(TOKEN_PREFIX + token) && !BizUtils.isNullOrEmpty(getTokenValue(token).toString());

    }

}
