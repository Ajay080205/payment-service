package com.company.payment_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.api-key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow login endpoint without API key
        if (path.startsWith("/auth")
        || path.startsWith("/swagger-ui")
        || path.startsWith("/v3/api-docs")) {

    filterChain.doFilter(request, response);
    return;
}

        String requestApiKey =
                request.getHeader("X-API-KEY");

        if (apiKey.equals(requestApiKey)) {

            filterChain.doFilter(
                    request,
                    response);

        } else {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter()
                    .write("Invalid API Key");
        }
    }
}