package com.iforddow.bizaudo.service.user.auth;

import com.iforddow.bizaudo.bo.user.auth.AuthBO;
import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.exception.BadRequestException;
import com.iforddow.bizaudo.exception.InvalidCredentialsException;
import com.iforddow.bizaudo.exception.ResourceExistsException;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.jpa.entity.user.UserProfile;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import com.iforddow.bizaudo.request.user.auth.*;
import com.iforddow.bizaudo.service.util_service.jwt.JwtService;
import com.iforddow.bizaudo.service.util_service.mail.MailService;
import com.iforddow.bizaudo.service.util_service.redis.RedisPasswordResetCodeService;
import com.iforddow.bizaudo.service.util_service.redis.RedisPasswordResetTokenService;
import com.iforddow.bizaudo.service.util_service.redis.RedisRefreshTokenService;
import com.iforddow.bizaudo.util.TokenHasher;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenHasher tokenHasher;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final RedisPasswordResetCodeService redisPasswordResetCodeService;
    private final MailService mailService;
    private final RedisPasswordResetTokenService redisPasswordResetTokenService;

    /**
     * A method to handle user registration.
     *
     * @param registerRequest The request object containing user registration details.
     * @return ResponseEntity containing UserDTO if registration is successful.
     * @author IFD
     * @since 2025-06-14
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> register(RegisterRequest registerRequest) throws BadRequestException {

        AuthBO authBO = new AuthBO();

        ArrayList<String> errors = authBO.validateUserRegistration(registerRequest);

        if (!errors.isEmpty()) {
            throw new BadRequestException("Registration failed: " + String.join(", ", errors));
        }

        // Check to ensure the password is not null or empty
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());

        // If a user with the same email already exists, throw an exception
        if (existingUser.isPresent()) {
            throw new ResourceExistsException("A user with this email already exists");
        }

        Instant currentTime = Instant.now();

        // Create a new user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(currentTime)
                .lastActive(currentTime)
                .build();

        UserProfile profile = UserProfile.builder()
                .createdAt(currentTime)
                .user(user)
                .build();

        user.setProfile(profile);

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("result", "User successfully registered"));

    }

    /**
     * A method to handle user login.
     *
     * @param loginRequest The request object containing user login details.
     * @return ResponseEntity containing UserDTO if login is successful.
     * @throws ResourceNotFoundException if the user is not found with the provided email.
     * @author IFD
     * @since 2025-06-15
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest, HttpServletResponse response) throws BadRequestException {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User email not found")
        );

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException ex) {

            if (ex instanceof BadCredentialsException) {
                throw new InvalidCredentialsException("Invalid credentials provided");
            }
            // Log or return the specific error
            throw new BadRequestException("Authentication failed: " + ex.getMessage());
        }

        String newAccessToken = createNewTokens(response, user);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

    }

    /**
     * A method to handle token refresh requests.
     *
     * @param refreshToken The refresh token from the request cookie.
     * @return ResponseEntity containing the new access token if refresh is successful.
     * @throws BadRequestException if the refresh token is invalid.
     * @author IFD
     * @since 2025-06-15
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> refreshToken(String refreshToken, HttpServletResponse response) throws BadRequestException {

        if(!jwtService.validateJwtToken(refreshToken)) {
            throw new BadRequestException("Invalid token");
        }

        String hashedRefreshToken = tokenHasher.hmacSha256(refreshToken);

        UUID userId = redisRefreshTokenService.getUserIdFromToken(hashedRefreshToken);

        if(userId == null) {
            throw new BadRequestException("Invalid token");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        redisRefreshTokenService.revokeToken(hashedRefreshToken);

        String newAccessToken = createNewTokens(response, user);

        // Update the user's last active time
        user.setLastActive(new Date().toInstant());

        // Save the updated user back to the database
        userRepository.save(user);

        UserDTO userDTO = new UserDTO(user, false);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "user", userDTO));


    }

    /**
     * A method to handle user logout.
     *
     * @param refreshToken The refresh token from the request cookie.
     * @return ResponseEntity indicating successful logout.
     * @author IFD
     * @since 2025-06-15
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> logout(String refreshToken, boolean allDevices, HttpServletResponse response) throws BadRequestException {

        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new BadRequestException("Invalid JWT token");
        }

        String hashedRefreshToken = tokenHasher.hmacSha256(refreshToken);

        if (allDevices) {

            UUID userId = redisRefreshTokenService.getUserIdFromToken(hashedRefreshToken);

            if (userId == null) {
                throw new BadRequestException("Refresh token not found or expired");
            }

            redisRefreshTokenService.revokeAllTokensForUser(userId);

        } else {

            redisRefreshTokenService.revokeToken(hashedRefreshToken);

        }

        // Invalidate the refresh token by setting it to null
        Cookie cookie = new Cookie("biz_rt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logout successful"));


    }

    /*
     * A method to change a users' password.
     *
     * @author IFD
     * @since 2025-07-22
     * */
    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest changePasswordRequest) {

        AuthBO authBO = new AuthBO();
        ArrayList<String> errors = authBO.validateChangePassword(changePasswordRequest);

        //If BO validation is faulty throw an error
        if(!errors.isEmpty()) {
            throw new BadRequestException("Unable to change password: " + String.join(", ", errors));
        }

        //Find user or throw an error
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //If old password is not users current password throw an error
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect old password");
        }

        //If new password is same as current password throw an error
        if(passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password");
        }

        //Finally set and save new user password
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

    /*
    * A method to request a new password due
    * to a user forgetting their old one.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public String requestForgotPassword(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        System.out.println("Searching for user: " + email);

        if(user.isPresent()) {

            String token = redisPasswordResetCodeService.generateAndStoreCode(user.get().getId());

            String content = "The following code can be used to reset your password: \n\n" +
                    token +
                    "\n\n" +
                    "This code will expire in 10 minutes.";

            mailService.sendMail(
                    email,
                    "Change Password Request",
                    content
            );

        }   else {
            System.out.println("User not found");
        }

        return "If an account with this email exists a code will be sent to reset your password";

    }

    /*
    * A method to check if the forgot password code
    * is correct. Give them a token if true.
    *
    * @author IFD
    * @since 2025-07-24
    * */
    public Map<String, String> checkForgotPasswordCode(ForgotPasswordCodeRequest forgotPasswordCodeRequest) {

        Optional<User> user = userRepository.findByEmail(forgotPasswordCodeRequest.getEmail());

        if(user.isEmpty()) {
            throw new BadRequestException("Invalid code received");
        }

        UUID userId = user.get().getId();
        String code = forgotPasswordCodeRequest.getCode();

        //See if code is valid.
        if(!redisPasswordResetCodeService.checkValid(userId, code)) {
            throw new BadRequestException("Invalid code received");
        }

        redisPasswordResetCodeService.deleteCode(userId);

        //Now that code is valid we can give them a token to reset
        //their password.
        String token = redisPasswordResetTokenService.generateAndStoreToken(userId);

        return Map.of("message", "Reset code successful",  "token", token);

    }

    public String forgotPasswordSubmitNew(ForgotPasswordSubmitRequest forgotPasswordSubmitRequest) {

        User user = userRepository.findByEmail(forgotPasswordSubmitRequest.getEmail()).orElseThrow(() -> new BadRequestException("Invalid token"));

        UUID userId = user.getId();
        String token = forgotPasswordSubmitRequest.getToken();
        String newPassword = forgotPasswordSubmitRequest.getNewPassword();
        String confirmNewPassword = forgotPasswordSubmitRequest.getConfirmNewPassword();

        if(!redisPasswordResetTokenService.validToken(userId, token)) {
            throw new BadRequestException("Invalid token");
        }

        if(!newPassword.equals(confirmNewPassword)) {
            throw new BadRequestException("New passwords do not match");
        }

        AuthBO authBO = new AuthBO();

        ArrayList<String> errors = authBO.validatePassword(newPassword);

        if(!errors.isEmpty()) {
            throw new BadRequestException("Unable to change password: " + String.join(", ", errors));
        }

        //Finally set and save new user password
        user.setPassword(passwordEncoder.encode(forgotPasswordSubmitRequest.getNewPassword()));

        userRepository.save(user);

        return "Password changed successfully";

    }

    /**
     * A method to create tokens for the user
     * upon logging in and refresh.
     *
     * @author IFD
     * @since 2025-07-18
     */
    private String createNewTokens(HttpServletResponse response, User user) {
        String newAccessToken = jwtService.generateJwtToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        String newHashedRefreshToken = tokenHasher.hmacSha256(newRefreshToken);

        redisRefreshTokenService.storeToken(newHashedRefreshToken, user.getId(), Instant.now().plusMillis(jwtService.jwtRefreshExpirationMs));

        Cookie refreshCookie = new Cookie("biz_rt", newRefreshToken);

        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(jwtService.jwtRefreshExpirationMs / 1000);
        refreshCookie.setSecure(true);

        response.addCookie(refreshCookie);

        return newAccessToken;
    }

}
