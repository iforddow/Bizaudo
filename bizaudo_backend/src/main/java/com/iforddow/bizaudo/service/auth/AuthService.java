package com.iforddow.bizaudo.service.auth;

import com.iforddow.bizaudo.bo.auth.RegisterBO;
import com.iforddow.bizaudo.exception.BadRequestException;
import com.iforddow.bizaudo.exception.InvalidCredentialsException;
import com.iforddow.bizaudo.exception.ResourceExistsException;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.impl.UserDetailsServiceImpl;
import com.iforddow.bizaudo.jpa.entity.User;
import com.iforddow.bizaudo.jpa.entity.UserProfile;
import com.iforddow.bizaudo.mapper.UserMapper;
import com.iforddow.bizaudo.repository.UserProfileRepository;
import com.iforddow.bizaudo.repository.UserRepository;
import com.iforddow.bizaudo.request.auth.LoginRequest;
import com.iforddow.bizaudo.request.auth.RegisterRequest;
import com.iforddow.bizaudo.service.jwt.JwtService;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserProfileRepository userProfileRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * A method to handle user registration.
     *
     * @param registerRequest The request object containing user registration details.
     *
     * @return ResponseEntity containing UserDTO if registration is successful.
     *
     * @author IFD
     *
     * @since  2025-06-14
     * */
    @Transactional
    public ResponseEntity<Map<String, Object>> register(RegisterRequest registerRequest, HttpServletResponse response) throws BadRequestException {

        RegisterBO  registerBO = new RegisterBO();

        ArrayList<String> errors = registerBO.validateUserRegistration(registerRequest);

        if(!errors.isEmpty()) {
            throw new BadRequestException("Registration failed: " + String.join(", ", errors));
        }

        // Check to ensure the password is not null or empty
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());

        // If a user with the same email already exists, throw an exception
        if(existingUser.isPresent()) {
            throw new ResourceExistsException("A user with this email already exists");
        }

        // Create a new user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .


        return ResponseEntity.ok(userMapper.toPublicDTO(user));

    }

    /**
     * A method to handle user login.
     *
     * @param loginRequest The request object containing user login details.
     *
     * @return ResponseEntity containing UserDTO if login is successful.
     *
     * @throws ResourceNotFoundException if the user is not found with the provided email.
     *
     * @author IFD
     * @since  2025-06-15
     * */
    public ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest, HttpServletResponse response) throws BadRequestException {

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        // Check if the user exists
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail());
        }

        Optional<UserProfile> userProfile = userProfileRepository.findByUser(user.get());

        if(userProfile.isEmpty()) {
            throw new ResourceNotFoundException("User profile not found for user with email: " + loginRequest.getEmail());
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException ex) {

            if(ex instanceof BadCredentialsException) {
                throw new InvalidCredentialsException("Invalid credentials provided");
            }
            // Log or return the specific error
            throw new BadRequestException("Authentication failed: " + ex.getMessage());
        }

        String accessToken = jwtService.generateJwtToken(user.get());
        String refreshToken = jwtService.generateRefreshToken(user.get());

        Cookie refreshCookie = new Cookie("biz_rt", refreshToken);

        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(jwtService.jwtRefreshExpirationMs / 1000);
        refreshCookie.setSecure(true);

        response.addCookie(refreshCookie);

        return ResponseEntity.ok(Map.of("accessToken", accessToken));

    }

    /**
     * A method to handle token refresh requests.
     *
     * @param refreshToken The refresh token from the request cookie.
     * @return ResponseEntity containing the new access token if refresh is successful.
     *
     * @throws BadRequestException if the refresh token is invalid.
     *
     * @author IFD
     * @since  2025-06-15
     * */
    public ResponseEntity<Map<String, Object>> refreshToken(String refreshToken) throws BadRequestException {

        if (jwtService.validateJwtToken(refreshToken)) {

            String id = jwtService.getUserIdFromToken(refreshToken);

            Optional<User> user = userRepository.findById(UUID.fromString(id));

            if (user.isEmpty()) {
                throw new ResourceNotFoundException("User not found with id: " + id);
            }

            String newAccessToken = jwtService.generateJwtToken(user.get());

            Optional<UserProfile> userProfile = userProfileRepository.findByUser(user.get());

            if (userProfile.isEmpty()) {
                throw new ResourceNotFoundException("User profile not found for user with email: " + email);
            }

            // Update the user's last active time
            user.get().setLastActive(new Date().toInstant());

            // Save the updated user back to the database
            userRepository.save(user.get());

            FullUserDTO userDTO = userMapper.toFullDTO(userMapper.toUser(userAuth.get(), userProfile.get()));

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "user", userDTO));

        } else {

            throw new BadRequestException("Invalid refresh token");

        }
    }

    /**
     * A method to handle user logout.
     *
     * @param refreshToken The refresh token from the request cookie.
     * @return ResponseEntity indicating successful logout.
     *
     * @author IFD
     * @since  2025-06-15
     * */
    public ResponseEntity<Map<String, Object>> logout(String refreshToken, HttpServletResponse response) throws BadRequestException {

        if (jwtService.validateJwtToken(refreshToken)) {

            // Invalidate the refresh token by setting it to null
            Cookie cookie = new Cookie("biz_rt", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setSecure(true);

            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("message", "Logout successful"));

        } else {

            throw new BadRequestException("Invalid refresh token");

        }
    }

}
