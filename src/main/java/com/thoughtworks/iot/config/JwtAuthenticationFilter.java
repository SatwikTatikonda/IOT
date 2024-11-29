package com.thoughtworks.iot.config;

import com.thoughtworks.iot.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        List<String>roles=null;
        String requestPath = request.getServletPath();

        System.out.println("in filter "+requestPath);

        if (requestPath.equals("/register") || requestPath.startsWith("/api/public/")) {
            filterChain.doFilter(request, response); // Skip processing
            return;
        }

        System.out.println("DoFilterInternal "+authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("extracting details");
            token = authHeader.substring(7);
            username = jwtUtil.extractUserName(token);
            roles=jwtUtil.extractRoles(token);
            System.out.println("roles extracted "+roles);
        }
        System.out.println("out of if ");
        if(!roles.isEmpty() && username!=null && roles!=null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = context.getBean(UserService.class).loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                System.out.println(authToken.toString());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }



    }
}
