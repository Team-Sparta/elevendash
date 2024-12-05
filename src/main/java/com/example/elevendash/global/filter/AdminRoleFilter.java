package com.example.elevendash.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AdminRoleFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/admin")) {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.setHeader("X-Content-Type-Options", "nosniff");
                response.setHeader("X-Frame-Options", "DENY");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Authentication is required");
                log.warn("Unauthorized access attempt to admin endpoint: {}", requestUri);
                return;
            }
            // Check if the user has the "ADMIN" role
            if (authentication.getAuthorities().stream()
                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
                response.setHeader("X-Content-Type-Options", "nosniff");
                response.setHeader("X-Frame-Options", "DENY");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Admin role required");
                log.warn("Forbidden access attempt to admin endpoint: {} by user: {}",
                        requestUri, authentication.getName());
                return;
            }
        }
        // Proceed with the next filter if user has admin role
        chain.doFilter(request, response);
    }
}