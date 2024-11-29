package com.thoughtworks.iot.service;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userService")
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
//
//
//    public UserDetails loadUserByUsername(User useR) throws UsernameNotFoundException {
//
//
//        // Convert the User entity to a Spring Security UserDetails object
//
//    }
//    static Long id=101L;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the database
        System.out.println("Fetching user for username: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        User user;

        if(!userOptional.isPresent()) {
//            System.out.println("User not found");
//            throw new UsernameNotFoundException(username);
            user=new User();
//            user.setId(id++);
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder(12).encode("admin123"));
            user.setRoles(List.of("ROLE_USER"));
            user=userRepository.save(user);
        }
        else{
            user=userOptional.get();

        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new) // Convert each role to a GrantedAuthority
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities

        );

    }
}
