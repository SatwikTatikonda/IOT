
package com.thoughtworks.iot.config;

import com.thoughtworks.iot.service.CustomUserServiceDetails;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtAuthenticationFilterTest {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ApplicationContext context;

    @MockBean
    private CustomUserServiceDetails customUserServiceDetails;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    @MockBean
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternal_SkipAuthPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/auth/login");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // No authentication set
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/data");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // No authentication set
    }

    @Test
    void testDoFilterInternal_InvalidAuthorizationHeader() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/data");
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // No authentication set
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "header.payload.signature";
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");

        when(request.getRequestURI()).thenReturn("/api/data");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUserName(token)).thenReturn(username);
        when(jwtUtil.extractRoles(token)).thenReturn(roles);
        when(context.getBean(CustomUserServiceDetails.class)).thenReturn(customUserServiceDetails);

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "password",
                authorities
        );
        when(customUserServiceDetails.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        assertNotNull(SecurityContextHolder.getContext().getAuthentication()); // Authentication set
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(username, authToken.getName());
        assertEquals(authorities, authToken.getAuthorities());
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "header.payload.signature";
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");

        when(request.getRequestURI()).thenReturn("/api/data");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUserName(token)).thenReturn(username);
        when(jwtUtil.extractRoles(token)).thenReturn(roles);
        when(context.getBean(CustomUserServiceDetails.class)).thenReturn(customUserServiceDetails);

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                "password",
                authorities
        );
        when(customUserServiceDetails.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(false); // Token validation fails

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // No authentication set
    }
}
