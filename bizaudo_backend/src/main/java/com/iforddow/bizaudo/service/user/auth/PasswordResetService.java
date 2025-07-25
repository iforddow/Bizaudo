package com.iforddow.bizaudo.service.user.auth;

import com.iforddow.bizaudo.bo.user.auth.AuthBO;
import com.iforddow.bizaudo.exception.BadRequestException;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import com.iforddow.bizaudo.request.user.auth.ChangePasswordRequest;
import com.iforddow.bizaudo.request.user.auth.ForgotPasswordSubmitRequest;
import com.iforddow.bizaudo.service.util_service.MailService;
import com.iforddow.bizaudo.service.util_service.redis.RedisPasswordResetTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

/**
* A service to reset or change a users' password.
* Handles when a user who is logged in wants to change
* their password, or when a user forgets their password.
*
* @author IFD
* @since 2025-07-24
* */
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisPasswordResetTokenService redisPasswordResetTokenService;
    private final MailService mailService;

    @Value("${frontend.base.url}")
    private String frontendURL;

    /**
     * A method to run when a user sends a request to
     * change their old password to a new one.
     *
     * @author IFD
     * @since 2025-07-22
     * */
    @Transactional
    public void changePasswordRequest(UUID userId, ChangePasswordRequest changePasswordRequest) {

        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();

        AuthBO authBO = new AuthBO();
        ArrayList<String> errors = authBO.validatePassword(oldPassword, newPassword, confirmNewPassword);

        //If BO validation is faulty throw an error
        if(!errors.isEmpty()) {
            throw new BadRequestException("Unable to change password: " + String.join(", ", errors));
        }

        //Find user or throw an error
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        changePassword(user, newPassword);
    }

    /**
    * A method to request a new password when a user
    * forgets their old password. Is responsible for
    * creating a token and sending an email to the user.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public void forgotPasswordRequest(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        //If user not null send an email
        if(user != null)  {

            UUID token = redisPasswordResetTokenService.generateAndStoreToken(user.getId());

            String content = "The following link can be used to reset your password: \n\n" +
                    frontendURL + "/forgot-password?token=" + token +
                    "\n\n" +
                    "This link will expire in 15 minutes.";

            mailService.sendMail(
                    email,
                    "Password Reset Link",
                    content
            );

        }

    }

    /**
    * A method for when a forgotten password request
    * is submitted, check the token, if good, approve
    * password change, then delete token.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    @Transactional
    public void forgotPasswordSubmit(ForgotPasswordSubmitRequest forgotPasswordSubmitRequest) {

        UUID token =  forgotPasswordSubmitRequest.getToken();
        String newPassword = forgotPasswordSubmitRequest.getNewPassword();
        String confirmNewPassword = forgotPasswordSubmitRequest.getConfirmNewPassword();

        if(!redisPasswordResetTokenService.validToken(token)) {
            throw new BadRequestException("Invalid token");
        }

        UUID userId = redisPasswordResetTokenService.getTokenValue(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String oldPassword = user.getPassword();

        boolean passwordSameAsOld = passwordEncoder.matches(newPassword, oldPassword);

        AuthBO authBO = new AuthBO();

        ArrayList<String> errors = new ArrayList<>(authBO.validatePassword(newPassword, confirmNewPassword));

        if(passwordSameAsOld) {
            errors.add("New password cannot be the same as the old password");
        }

        if(!errors.isEmpty()) {
            throw new BadRequestException("Unable to change password: " + String.join(", ", errors));
        }

        changePassword(user, newPassword);

        redisPasswordResetTokenService.deleteToken(token);

    }

    /**
    * A method to change a users' password. This
    * should only ever be run by other methods.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    private void changePassword(User user, String newPassword) {

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

    }

}
