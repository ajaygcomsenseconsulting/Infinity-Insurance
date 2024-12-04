package com.infinityinsurance.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    // Retrieve secret key from environment variable or application.properties for production security
    @Value("${jwt.secret}")
    private String secret;

    private final long expirationTime = 3600000; // 1 hour in milliseconds

    // Generate JWT Token
    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }

    // Validate JWT Token and return the decoded JWT object
    public DecodedJWT validateToken(String token) throws InvalidTokenException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // Log the error and throw a custom exception
            throw new InvalidTokenException("Invalid or expired token: " + e.getMessage());
        }
    }

    // Optional: You can create a utility method to check token expiration time
    public boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }
}
