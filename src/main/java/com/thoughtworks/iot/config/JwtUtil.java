package com.thoughtworks.iot.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    private  String secretkey = "";


    public JwtUtil() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate JWT Token
    public String generateToken(String username, List<String> roles) {

        try{
            String token =
                    Jwts.builder()
                    .subject(username)
                    .claim("roles", roles)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                    .signWith(getKey())
                    .compact();
            return token;
        }
        catch(Exception e){
            System.out.println("message "+e.getMessage());
        }
        return null;

    }

    public SecretKey getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return  extractClaim(token,Claims::getSubject);
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        System.out.println(
                "Claims "+claims
        );
        return claims.get("roles", List.class);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }




//    public List<String> extractRoles(String token) {
//
//        return null;
//    }

//    public boolean validateToken(String token, UserDetails userDetails) {
//        return true;
//    }

//     Validate Token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername = extractUserName(token);
        final List<String> extractedRoles = extractRoles(token);
        System.out.println("extractedRoles "+extractedRoles);
        System.out.println(userDetails.getAuthorities());
        return (extractedUsername.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }
//
//    // Extract Username
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

    // Extract Roles

//
//    // Check Token Expiry
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract Expiration Date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
//
//    // Extract Claims
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//    }
}
