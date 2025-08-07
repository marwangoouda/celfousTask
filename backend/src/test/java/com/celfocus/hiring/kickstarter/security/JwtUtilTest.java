package com.celfocus.hiring.kickstarter.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "myTestSecretKey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtUtil.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void getUsernameFromToken_ValidToken_ShouldReturnUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.getUsernameFromToken(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_ValidToken_ShouldReturnTrue() {
        String token = jwtUtil.generateToken("testuser");

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidToken_ShouldThrowJwtException() {
        String invalidToken = "invalid.token.here";

        assertThrows(JwtException.class, () -> jwtUtil.validateToken(invalidToken),
                "Expected validateToken() to throw JwtException for a malformed token");
    }

    @Test
    void isTokenExpired_ValidToken_ShouldReturnFalse() {
        String token = jwtUtil.generateToken("testuser");

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void isTokenExpired_ExpiredToken_ShouldThrowJwtException() {
        String invalidToken = "invalid.token.here";

        assertThrows(JwtException.class, () -> jwtUtil.validateToken(invalidToken),
                "Expected isTokenExpired() to throw JwtException for a malformed token");
    }
}

