package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.config.JwtUtil;
import com.thoughtworks.iot.dtos.AuthRequest;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authenticate")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public String generateToken(@RequestBody AuthRequest user) {
        System.out.println(user);

        try{
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

        }
        catch(AuthenticationException e){
            System.out.println(
                    e.getMessage()
            );
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        System.out.println("UserDetails: " + userDetails);
        System.out.println(userDetails);
        String token = jwtUtil.generateToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority()) // Extract authority names as strings
                        .collect(Collectors.toList()) // Convert to a list of strings
        );
        System.out.println(token);
        return token;
    }
}

