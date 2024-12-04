package com.infinityinsurance.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf().disable() // Disabling CSRF for API endpoints
            .authorizeExchange()
            .pathMatchers("/auth/**", "/api/users/register", "/api/users/validate").permitAll() // Public endpoints
            .anyExchange().authenticated() // All other endpoints require authentication
            .and()
            .httpBasic(); // Optional: Add basic authentication for debugging purposes
        return http.build();
    }
}
