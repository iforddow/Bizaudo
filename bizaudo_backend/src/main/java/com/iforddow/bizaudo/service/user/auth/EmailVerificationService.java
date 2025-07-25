package com.iforddow.bizaudo.service.user.auth;

import com.iforddow.bizaudo.exception.BadRequestException;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import com.iforddow.bizaudo.service.util_service.MailService;
import com.iforddow.bizaudo.service.util_service.redis.RedisEmailVerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
* A service to verify a users' email.
*
* @author IFD
* @since 2025-07-24
* */
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final MailService mailService;
    private final RedisEmailVerificationTokenService redisEmailVerificationTokenService;
    private final UserRepository userRepository;

    @Value("${frontend.base.url}")
    private String frontendURL;

    /**
    * A method to send a verification email so a
    * user can verify their email.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public void sendVerificationEmail(String email) {

        UUID token = redisEmailVerificationTokenService.generateAndStoreToken(email);

        String content = "The following link can be used to reset your password: \n\n" +
                frontendURL + "/verify-email?token=" + token +
                "\n\n" +
                "This link will expire in 30 minutes.";

        mailService.sendMail(
                email,
                "Verify Email",
                content
        );

    }

    /**
    * A method to verify a users email, will check
    * the redis db for verification.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    @Transactional
    public void verifyEmail(UUID token) {

        if(!redisEmailVerificationTokenService.validToken(token)) {
            throw new BadRequestException("Invalid token");
        };

        String userEmail = redisEmailVerificationTokenService.getTokenValue(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmailVerified(true);

        userRepository.save(user);

        redisEmailVerificationTokenService.deleteToken(token);

    }

}
