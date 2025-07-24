package com.iforddow.bizaudo.service.util_service.redis;

import com.iforddow.bizaudo.redis.templates.RedisObjectTemplate;
import com.iforddow.bizaudo.util.BizUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisPasswordResetCodeService {

    private final RedisObjectTemplate redisObjectTemplate;
    private final RedisConnectionFactory redisConnectionFactory;

    private RedisTemplate<String, Object> redisTemplate;

    private static final String CODE_PREFIX = "passwordResetCode:";
    private static final Duration CODE_TTL = Duration.ofMinutes(10);
    private static final SecureRandom secureRandom = new SecureRandom();

    @PostConstruct
    private void init() {
        this.redisTemplate = redisObjectTemplate.redisTemplate(redisConnectionFactory);
    }

    /*
    * A method to generate and store a new token
    * for resetting a user password.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public String generateAndStoreCode(UUID userId) {
        String code;
        String key;

        //If a code already exists for this user, delete it
        //as they are requesting a new one.
        if(redisTemplate.hasKey(CODE_PREFIX + userId.toString())) {
            deleteCode(userId);
        }

        //Generate a random code that is not already in the
        //redis db. Doesn't even have to be that random cause
        //the key is the user id but whatever.
        do {
            code = String.format("%06d", secureRandom.nextInt(1_000_000));
            key = CODE_PREFIX + userId;
        } while (redisTemplate.hasKey(key));

        //Set code
        redisTemplate.opsForValue().set(key, code, CODE_TTL);

        //Return code
        return code;
    }

    /**
    * A method to get a code currently
    * in the redis db.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public String getCode(UUID userId) {

        Object value = redisTemplate.opsForValue().get(CODE_PREFIX + userId);
        return value != null ? value.toString() : null;

    }

    /**
    * A method to delete a code.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public void deleteCode(UUID userId) {
        redisTemplate.delete(CODE_PREFIX + userId);
    }

    /**
    * A method to check if the code is valid.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public boolean checkValid(UUID userId, String code) {

        if(!redisTemplate.hasKey(CODE_PREFIX + userId)) {
            return false;
        }

        if(BizUtils.isNullOrEmpty(getCode(userId))) {
            return false;
        }

        return getCode(userId).equals(code);
    }

}
