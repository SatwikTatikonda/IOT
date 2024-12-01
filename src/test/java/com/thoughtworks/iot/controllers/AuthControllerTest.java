package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.dtos.AuthRequest;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @MockBean
    private UserService userService;

    @Test
    void givenAuthRequestDtoWhenResigerThenUserIsCreated() {

        User mockUser = new User("clksncjd", "username", List.of("ROLE_USER"),"password");
        AuthRequest authRequest=new AuthRequest(1L,"username","password");
        when(userService.reigsterUser(authRequest.getUsername(),authRequest.getPassword())).thenReturn(mockUser);

        ResponseEntity<User>register=authController.register(authRequest);

        assertEquals(mockUser,register.getBody());
        assertEquals(200,register.getStatusCode().value());

        verify(userService,times(1)).reigsterUser(authRequest.getUsername(),authRequest.getPassword());

    }

    @Test
    void givenAuthRequestDtoWhenLoginThenJWTTokenIsCreated() {

        String mock_token="dfcknDN23QSCE";
        AuthRequest authRequest=new AuthRequest(1L,"username","password");
        when(userService.loginUser(authRequest.getUsername(),authRequest.getPassword())).thenReturn(mock_token);
        ResponseEntity<String>register=authController.login(authRequest);
        assertEquals(mock_token,register.getBody());
        assertEquals(200,register.getStatusCode().value());
        verify(userService,times(1)).loginUser(authRequest.getUsername(),authRequest.getPassword());


    }
}