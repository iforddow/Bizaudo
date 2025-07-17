package com.iforddow.bizaudo.filter;

import com.iforddow.bizaudo.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that checks for JWT tokens in the Authorization header of HTTP requests.
 * If a valid JWT token is found, it authenticates the user and sets the security context.
 * This filter extends OncePerRequestFilter to ensure it is executed once per request.
 *
 * @author IFD
 * @since  2025-06-15
 * */
public class JwtFilter extends OncePerRequestFilter {

    // Initialize the JwtService
    private final JwtService jwtService;

    // Initialize the UserDetailsService
    private final UserDetailsService userDetailsService;

    /**
     * A constructor for the JwtFilter class.
     *
     * @param jwtService The service used for JWT operations.
     * @param userDetailsService The service used to load user details.
     *
     * @author IFD
     * @since 2025-06-15
     * */
    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * A filter that intercepts HTTP requests to check for JWT tokens in the Authorization header.
     * If a valid JWT token is found, it authenticates the user and sets the security context.
     *
     * @param request The HTTP request to filter.
     * @param response The HTTP response to filter.
     * @param filterChain The filter chain to continue processing the request.
     *
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an I/O error occurs during filtering.
     *
     * @author IFD
     * @since 2025-06-15
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String username = jwtService.getUserIdFromToken(jwtToken);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtService.validateJwtToken(jwtToken)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }

}
