package com.company.payment_service.auth;

import com.company.payment_service.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody LoginRequest request) {

        if (!"admin".equals(request.getUsername())
                || !"admin123".equals(request.getPassword())) {

            throw new RuntimeException(
                    "Invalid Username or Password");
        }

        String token =
                jwtUtil.generateToken(
                        request.getUsername());

        Map<String, String> response =
                new HashMap<>();

        response.put("token", token);

        return response;
    }
}