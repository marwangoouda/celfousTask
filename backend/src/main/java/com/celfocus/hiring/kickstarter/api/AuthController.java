package com.celfocus.hiring.kickstarter.api;

import com.celfocus.hiring.kickstarter.api.dto.LoginRequest;
import com.celfocus.hiring.kickstarter.api.dto.LoginResponse;
import com.celfocus.hiring.kickstarter.exception.InvalidCredsException;
import com.celfocus.hiring.kickstarter.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Authentication controller for user login and JWT token generation.
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Login endpoint that generates JWT token for valid users.
     * For demo purposes, accepts any username with password "password".
     */
    @PostMapping("/login")
    public @ResponseBody LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for username: {}", loginRequest.getUsername());

        if ("password".equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            logger.info("Login successful for username: {}", loginRequest.getUsername());

            return new LoginResponse(token, loginRequest.getUsername());
        } else {
            throw InvalidCredsException.forUser(loginRequest.getUsername());
        }
    }

    /**
     * Validate token endpoint.
     */
    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        jwtUtil.validateToken(token);
        return ResponseEntity.ok().build();
    }

}

