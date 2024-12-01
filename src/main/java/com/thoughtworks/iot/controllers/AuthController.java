package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.config.JwtUtil;
import com.thoughtworks.iot.dtos.AuthRequest;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
public class AuthController {


    private  UserService userService;

    public AuthController(UserService userService) {
//
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest authRequest) {

        return ResponseEntity.ok(userService.reigsterUser(authRequest.getUsername(), authRequest.getPassword()));
    }

    @PostMapping("/auth/login")
    public  ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {

        System.out.println(authRequest.toString());
        return  ResponseEntity.ok(userService.loginUser(authRequest.getUsername(),authRequest.getPassword()));
    }

    }


