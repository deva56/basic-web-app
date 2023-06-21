package com.futurefactorytech.reviewer.application_services.services.jwt;

import com.futurefactorytech.reviewer.api.services.jwt.JwtService;
import com.futurefactorytech.reviewer.api.types.jwt.JwtOperationResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Named
public class JwtServiceImpl implements JwtService {

    @Value("${application-config.jwt.secret-key}")
    private String JWT_SECRET_KEY;

    @Override
    public JwtOperationResult<String, Exception> extractUsernameOrEmail(String token) {
        try {
            String result = extractClaim(token, Claims::getSubject);
            return new JwtOperationResult<>(result, null);
        } catch (Exception e) {
            return new JwtOperationResult<>(null, e);
        }
    }

    @Override
    public JwtOperationResult<String, Exception> extractCustomClaim(String token, String claimName) {
        try {
            String result = extractCustomMapClaim(token, claimName);
            return new JwtOperationResult<>(result, null);
        } catch (Exception e) {
            return new JwtOperationResult<>(null, e);
        }
    }

    @Override
    public JwtOperationResult<Map<String, Object>, Exception> extractAllClaimsMap(String token) {
        try {
            Map<String, Object> result = extractAllClaims(token);
            return new JwtOperationResult<>(result, null);
        } catch (Exception e) {
            return new JwtOperationResult<>(null, e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractCustomMapClaim(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName, String.class);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, String username, Integer expirationTime) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public JwtOperationResult<Boolean, Exception> isTokenValid(String token) {
        try {
            Boolean result = !isTokenExpired(token);
            return new JwtOperationResult<>(result, null);
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | UnsupportedJwtException |
                 MalformedJwtException e) {
            return new JwtOperationResult<>(null, e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
