package com.iforddow.bizaudo.controller.user.auth;

import com.iforddow.bizaudo.request.user.auth.LoginRequest;
import com.iforddow.bizaudo.request.user.auth.RegisterRequest;
import com.iforddow.bizaudo.service.user.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


}
