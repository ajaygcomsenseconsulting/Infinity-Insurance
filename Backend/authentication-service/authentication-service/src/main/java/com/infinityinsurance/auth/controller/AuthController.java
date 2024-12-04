package com.infinityinsurance.auth.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.infinityinsurance.auth.service.InvalidTokenException;
import com.infinityinsurance.auth.service.JwtTokenProvider;
import com.infinityinsurance.auth.service.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @Autowired
    private DiscoveryClient discoveryClient;

    public String getUserServiceUrl() {
        return discoveryClient.getInstances("USER-SERVICE")
                              .stream()
                              .findFirst()
                              .map(ServiceInstance::getUri)
                              .map(Object::toString)
                              .orElseThrow(() -> new IllegalStateException("USER-SERVICE not found"));
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
        // Reactive call to USER-SERVICE
        String userServiceUrl = getUserServiceUrl() + "/api/users/validate";

        return webClientBuilder.build()
                .post()
                .uri(userServiceUrl)
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(isValidUser -> {
                    if (Boolean.TRUE.equals(isValidUser)) {
                        // Generate JWT token
                        String token = jwtTokenProvider.generateToken(loginRequest.getUsername(), "USER");
                        return Mono.just(ResponseEntity.ok(Map.of("token", token)));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Invalid credentials")));
                    }
                })
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(Map.of("error", "Unable to connect to User Service"))));
    }

    @GetMapping("/validate")
    public Mono<ResponseEntity<String>> validateToken(HttpServletRequest request) {
        // Retrieve the Authorization header (Bearer token)
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Mono.just(ResponseEntity.status(400).body("Token not found in request"));
        }

        try {
            // Extract and validate the token
            DecodedJWT decodedJWT = jwtTokenProvider.validateToken(token.substring(7)); // Remove "Bearer " prefix
            if (jwtTokenProvider.isTokenExpired(decodedJWT)) {
                return Mono.just(ResponseEntity.status(401).body("Token expired"));
            }

            // Token is valid
            return Mono.just(ResponseEntity.ok("Token is valid"));

        } catch (InvalidTokenException e) {
            return Mono.just(ResponseEntity.status(401).body("Invalid token: " + e.getMessage()));
        }
    }

//    @GetMapping("/discovery")
//    public ResponseEntity<?> discoverServices() {
//        List<ServiceInstance> userServiceInstances = discoveryClient.getInstances("USER-SERVICE");
//        if (userServiceInstances.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER-SERVICE not found in Eureka");
//        }
//        return ResponseEntity.ok(userServiceInstances);
//    }

}
