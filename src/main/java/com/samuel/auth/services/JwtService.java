package com.samuel.auth.services;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secretKey;

    @Value("${jwt.access-token-expiration-time}")
    private Long accessTokenExpirationTimeInMillis;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long refreshTokenExpirationTimeInMillis;

    public JwtService() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encodedSecretKey = secretKey.getEncoded();
        this.secretKey = Base64.getEncoder().encodeToString(encodedSecretKey);
    }


    public SecretKey getKey() {
        byte[] decodedSecretKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(decodedSecretKey);
    }

    public Map<String, String> generateToken(String id, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        String accessToken = buildToken(id, accessTokenExpirationTimeInMillis, claims);
        String refreshToken = buildToken(id, refreshTokenExpirationTimeInMillis, claims);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }
    
    public String buildToken(String subject, Long expirationTime, Map<String, Object> claims) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getKey())
                .compact();
        
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
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

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
