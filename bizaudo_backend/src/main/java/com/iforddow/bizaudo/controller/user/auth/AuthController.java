package com.iforddow.bizaudo.controller.user.auth;

import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.request.user.auth.*;
import com.iforddow.bizaudo.service.user.auth.AuthService;
import com.iforddow.bizaudo.service.user.auth.EmailVerificationService;
import com.iforddow.bizaudo.service.user.auth.PasswordResetService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@CookieValue("biz_rt") String refreshToken, HttpServletResponse response) {
        return authService.refreshToken(refreshToken, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@CookieValue("biz_rt") String refreshToken, @RequestParam(name = "allDevices") boolean allDevices, HttpServletResponse response) {
        return authService.logout(refreshToken, allDevices, response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPasswordRequest(@RequestParam String email) {

        passwordResetService.forgotPasswordRequest(email);

        return ResponseEntity.ok("If an account with this email exists, a reset link will be sent");

    }

    @PostMapping("/forgot-password/submit")
    public ResponseEntity<String> forgotPasswordSubmit(@RequestBody ForgotPasswordSubmitRequest forgotPasswordSubmitRequest) {

        passwordResetService.forgotPasswordSubmit(forgotPasswordSubmitRequest);

        return ResponseEntity.ok("Password reset was successful");

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();

        passwordResetService.changePasswordRequest(userId, changePasswordRequest);

        return ResponseEntity.ok("Password changed successfully");

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify-email")
    public ResponseEntity<String> requestEmailVerification(@RequestParam String email) {

        emailVerificationService.sendVerificationEmail(email);

        return ResponseEntity.ok("Email verification email sent");

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify-email/submit")
    public ResponseEntity<String> verifyEmail(@RequestParam UUID token) {

        emailVerificationService.verifyEmail(token);

        return ResponseEntity.ok("Email verified successfully");

    }

}
