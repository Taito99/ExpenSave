package com.amadeusz.ExpensesTracker.authentication;

import com.amadeusz.ExpensesTracker.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getServletPath();

        // Skip public endpoints
        if ("/api/v1/user/sign-up".equals(path) || "/api/v1/user/sign-in".equals(path)) {
            log.debug("Public endpoint accessed: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        String username;

        try {
            username = jwtService.extractUsername(jwtToken); // Extracting username from JWT
        } catch (Exception e) {
            log.error("Failed to extract username from JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = userService.loadUserByUsername(username); // Load user by username
            } catch (Exception ex) {
                log.error("Failed to load user details for username {}: {}", username, ex.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                log.info("User authenticated successfully: {}", username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                log.warn("Invalid JWT token for username: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
