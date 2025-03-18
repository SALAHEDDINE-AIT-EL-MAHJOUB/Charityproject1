package org.example.charityproject1.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.example.charityproject1.util.JwtUtil;
import org.example.charityproject1.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // List of paths that should be excluded from JWT authentication
    private List<String> excludedPaths = Arrays.asList("/auth/", "/login/", "/css/", "/js/", "/images/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip JWT validation for excluded paths
        String path = request.getRequestURI();
        boolean shouldSkip = excludedPaths.stream().anyMatch(path::contains);

        if (shouldSkip) {
            filterChain.doFilter(request, response);
            return;
        }

        // Try to get token from session first (for web views)
        String jwt = (String) request.getSession().getAttribute("jwt_token");

        // If not in session, try header
        if (jwt == null) {
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
            }
        }

        // Process the token if found
        if (jwt != null) {
            try {
                String username = jwtUtil.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set authentication in SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        // Store SecurityContext in session for web views
                        HttpSession session = request.getSession(true);
                        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                    }
                }
            } catch (Exception e) {
                System.err.println("Invalid JWT token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return excludedPaths.stream().anyMatch(path::contains);
    }
}