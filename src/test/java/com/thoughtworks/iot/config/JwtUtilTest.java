package com.thoughtworks.iot.config;

import com.thoughtworks.iot.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;


    @Test
    void generateToken() {

        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generateToken(username, roles);
        assertNotNull(token, "Generated token should not be null");
        assertTrue(token.length() > 0, "Generated token should not be empty");
    }

    @Test
    void testExtractUserName() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generateToken(username, roles);

        String extractedUsername = jwtUtil.extractUserName(token);

        assertEquals(username, extractedUsername, "Extracted username should match the original username");
    }

    @Test
    void testExtractRoles() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generateToken(username, roles);

        List<String> extractedRoles = jwtUtil.extractRoles(token);

        assertEquals(roles, extractedRoles, "Extracted roles should match the original roles");
    }

    @Test
    void testValidateToken() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generateToken(username, roles);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new) // Convert each role to a GrantedAuthority
                .collect(Collectors.toList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "password",
                authorities

        );
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid, "The token should be valid for the given UserDetails");
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generateToken(username, roles);
        System.out.println(token);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new) // Convert each role to a GrantedAuthority
                .collect(Collectors.toList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "password",
                authorities

        );
        boolean isExpired = jwtUtil.validateToken(token,userDetails);
        System.out.println(isExpired);
        assertTrue(isExpired, "The token should not be expired");
    }


}