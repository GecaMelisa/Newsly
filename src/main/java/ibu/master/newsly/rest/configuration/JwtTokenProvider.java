package ibu.master.newsly.rest.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate a JWT token without the "Bearer " prefix
     */
    public String generateToken(Long userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId);
        claims.put("sub", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract user ID from the JWT token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("user_id", Long.class);
    }

    /**
     * Extract email from the JWT token
     */
    public String getEmailFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("sub", String.class);
    }

    /**
     * Validate the JWT token
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract all claims from the token
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
