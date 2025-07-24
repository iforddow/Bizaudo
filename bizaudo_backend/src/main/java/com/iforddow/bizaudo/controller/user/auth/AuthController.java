package com.iforddow.bizaudo.controller.user.auth;

import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.request.user.auth.*;
import com.iforddow.bizaudo.service.user.auth.AuthService;
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
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");

        return ResponseEntity.ok(authService.requestForgotPassword(email));

    }

    @PostMapping("/forgot-password/check-code")
    public ResponseEntity<Map<String, String>> forgotPasswordCheck(@RequestBody ForgotPasswordCodeRequest forgotPasswordCodeRequest) {

        return ResponseEntity.ok(authService.checkForgotPasswordCode(forgotPasswordCodeRequest));

    }

    @PostMapping("/forgot-password/submit-new")
    public ResponseEntity<String> forgotPasswordSubmitNew(@RequestBody ForgotPasswordSubmitRequest forgotPasswordRequest) {

        return ResponseEntity.ok(authService.forgotPasswordSubmitNew(forgotPasswordRequest));

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();

        authService.changePassword(userId, changePasswordRequest);

        return ResponseEntity.ok("Password changed successfully");

    }

}
