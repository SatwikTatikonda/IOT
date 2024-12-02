package com.thoughtworks.iot.service;

import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomUserServiceDetailsTest {


    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CustomUserServiceDetails customUserServiceDetails;

    User user;
    @BeforeEach
    public void setUp() {
        // Create a test user
        user = new User("skncjalkk","user123", List.of("ROLE_USER"),"password123");
    }


    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("user123")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserServiceDetails.loadUserByUsername("user123");

        // Assert
        assertNotNull(userDetails);
        assertEquals("user123", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByUsername("user123");
    }



//    @Test
//    void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
//        // Arrange
//        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(UsernameNotFoundException.class, () -> {
//            customUserServiceDetails.loadUserByUsername("nonExistingUser");
//        });
//
//        verify(userRepository, times(1)).findByUsername("nonExistingUser");
//    }
}