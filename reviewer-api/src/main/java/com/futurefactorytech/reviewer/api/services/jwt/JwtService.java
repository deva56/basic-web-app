package com.futurefactorytech.reviewer.api.services.jwt;

import com.futurefactorytech.reviewer.api.types.jwt.JwtOperationResult;

import java.util.Map;

public interface JwtService {

    JwtOperationResult<String, Exception> extractUsernameOrEmail(String token);

    JwtOperationResult<String, Exception> extractCustomClaim(String token, String claimName);

    JwtOperationResult<Map<String, Object>, Exception> extractAllClaimsMap(String token);

    JwtOperationResult<Boolean, Exception> isTokenValid(String token);

    String generateToken(Map<String, Object> extraClaims, String username, Integer expirationTime);

}
