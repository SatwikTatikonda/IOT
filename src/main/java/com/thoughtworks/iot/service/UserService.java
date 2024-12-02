package com.thoughtworks.iot.service;
import com.thoughtworks.iot.Exception.UserAlreadyRegistered;
import com.thoughtworks.iot.Exception.UserNotFoundException;
import com.thoughtworks.iot.config.JwtUtil;
import com.thoughtworks.iot.dtos.AuthRequest;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,UserDetailsService userDetailsService,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService=userDetailsService;
        this.jwtUtil = jwtUtil;
    }




    public User registerUser(String Username, String Password) throws UserAlreadyRegistered {
        Optional<User> user = userRepository.findByUsername(Username);
        if(user.isPresent()) {
            throw new UserAlreadyRegistered("User already registerd");
        }
        User u=new User();
        u.setUsername(Username);
        u.setPassword(passwordEncoder.encode(Password));
        u.setRoles(List.of("ROLE_ADMIN"));
        return userRepository.save(u);
    }

    public String loginUser(String username, String password) {
        System.out.println(userRepository.findAll());
        System.out.println("username;" + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(!userOptional.isPresent()) {
//            throw new UserNotFoundException("user not regisrted,try rgister first");
            throw new UsernameNotFoundException("Username not found");
        }
        System.out.println(userOptional);
        System.out.println(userOptional.isPresent());
        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Username or password is incorrect");
        }
        else{


            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails: " + userDetails);
            String token = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(grantedAuthority -> grantedAuthority.getAuthority()) // Extract authority names as strings
                            .collect(Collectors.toList()) // Convert to a list of strings
            );
            return token;

        }
    }
}