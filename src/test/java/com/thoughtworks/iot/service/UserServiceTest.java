package com.thoughtworks.iot.service;

import com.thoughtworks.iot.Exception.UserAlreadyRegistered;
import com.thoughtworks.iot.config.JwtUtil;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {



//        @Mock
    @MockBean
        private UserRepository userRepository;

//        @Mock
    @MockBean
        private UserDetailsService userDetailsService;

//        @Mock
        @MockBean
        private PasswordEncoder passwordEncoder;

//        @Mock
    @MockBean
        private JwtUtil jwtUtil;

//        @InjectMocks
    @Autowired
        private UserService userService;

        @Test
        void testRegisterUser_Success () throws UserAlreadyRegistered {
            String username = "newUser";
            String password = "password";

            User newUser =new User("kFSJNDJ", "newUser", List.of("ROLE_ADMIN"),"encodedPassword");

            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
            when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(newUser);

            User result = userService.registerUser(username, password);

            System.out.println(result.toString());
            assertEquals(username, result.getUsername());
            assertEquals("encodedPassword", result.getPassword());
            assertEquals(List.of("ROLE_ADMIN"), result.getRoles());


        }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        String username = "existingUser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyRegistered.class, () -> userService.registerUser(username, password));
        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        String username = "existingUser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String token = "generatedToken";

        User mockUser = new User("kFSJNDJ",username, List.of("ROLE_USER"),encodedPassword);
        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(encodedPassword)
                .roles("USER")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(eq(username), anyList())).thenReturn(token);

        String result = userService.loginUser(username, password);

        assertEquals(token, result);
    }


    @Test
    void testLoginUser_UserNotFound() {
        String username = "nonExistentUser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loginUser(username, password));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyList());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        String username = "existingUser";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";

        User mockUser = new User("sNCJ",username,List.of("ROLE_USER"),encodedPassword);
        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(encodedPassword)
                .roles("USER")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        // Simulate password mismatch
        doThrow(new UsernameNotFoundException("Username or password is incorrect"))
                .when(userDetailsService).loadUserByUsername(anyString());

        assertThrows(UsernameNotFoundException.class, () -> userService.loginUser(username, password));
    }

    @Test
    void testGenerateToken_Success() {
        String username = "testUser";
        String token = "jwtToken";

        when(jwtUtil.generateToken(eq(username), anyList())).thenReturn(token);

        String result = jwtUtil.generateToken(username, List.of("ROLE_USER"));
        assertEquals(token, result);
    }


    @Test
    void testRegisterUser_RoleAssignment() throws UserAlreadyRegistered {
        String username = "user";
        String password = "pass";
        User newUser=new User("jwfnkds",username,List.of("ROLE_ADMIN"),password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.registerUser(username, password);
        assertEquals(List.of("ROLE_ADMIN"), result.getRoles());
    }




    }

