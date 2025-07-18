package com.iforddow.bizaudo.service.user.auth;

import com.iforddow.bizaudo.bo.user.auth.RegisterBO;
import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.exception.BadRequestException;
import com.iforddow.bizaudo.exception.InvalidCredentialsException;
import com.iforddow.bizaudo.exception.ResourceExistsException;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.rbac.RefreshToken;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.jpa.entity.user.UserProfile;
import com.iforddow.bizaudo.repository.auth.RefreshTokenRepository;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import com.iforddow.bizaudo.request.user.auth.LoginRequest;
import com.iforddow.bizaudo.request.user.auth.RegisterRequest;
import com.iforddow.bizaudo.service.jwt.JwtService;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHasher tokenHasher;

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

        RegisterBO registerBO = new RegisterBO();

        ArrayList<String> errors = registerBO.validateUserRegistration(registerRequest);

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

        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(hashedRefreshToken).orElseThrow(
                () -> new ResourceNotFoundException("Refresh token not found")
        );

        if (storedToken.getExpiresAt().isBefore(Instant.now()) || storedToken.getRevoked()) {
            throw new BadRequestException("Invalid refresh token");
        }

        User user = storedToken.getUser();

        storedToken.setRevoked(true);

        refreshTokenRepository.save(storedToken);

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

        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(hashedRefreshToken).orElseThrow(
                () -> new ResourceNotFoundException("Refresh token not found")
        );

        if(allDevices) {
            List<RefreshToken> userRefreshTokens = refreshTokenRepository.findAllByUser(storedToken.getUser());

            for (RefreshToken userRefreshToken : userRefreshTokens) {
                userRefreshToken.setRevoked(true);
            }

            refreshTokenRepository.saveAll(userRefreshTokens);
        }   else {
            storedToken.setRevoked(true);

            refreshTokenRepository.save(storedToken);
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

        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .user(user)
                .tokenHash(newHashedRefreshToken)
                .expiresAt(Instant.now().plusMillis(jwtService.jwtRefreshExpirationMs))
                .revoked(false)
                .createdAt(Instant.now())
                .build();

        refreshTokenRepository.save(newRefreshTokenEntity);

        Cookie refreshCookie = new Cookie("biz_rt", newRefreshToken);

        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(jwtService.jwtRefreshExpirationMs / 1000);
        refreshCookie.setSecure(true);

        response.addCookie(refreshCookie);

        return newAccessToken;
    }

}
