package com.iforddow.bizaudo.controller.auth;

import com.iforddow.bizaudo.request.auth.LoginRequest;
import com.iforddow.bizaudo.request.auth.RegisterRequest;
import com.iforddow.bizaudo.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        return authService.register(registerRequest, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@CookieValue("biz_rt") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@CookieValue("biz_rt") String refreshToken, HttpServletResponse response) {
        return authService.logout(refreshToken, response);
    }


}
