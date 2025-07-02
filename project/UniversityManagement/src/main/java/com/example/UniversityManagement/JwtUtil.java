package com.example.UniversityManagement;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey"; // should be at least 256 bits
    private final long JWT_EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours
    
    // In-memory token blacklist for logout functionality
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        // Check if token is blacklisted
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    
    // Add token to blacklist for logout
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
    
    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    
    // Clean up expired tokens from blacklist (can be called periodically)
    public void cleanupExpiredTokens() {
        blacklistedTokens.removeIf(token -> {
            try {
                return isTokenExpired(token);
            } catch (Exception e) {
                // If we can't parse the token, remove it from blacklist
                return true;
            }
        });
    }
}
