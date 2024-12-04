package com.infinityinsurance.auth.config;

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
        return http.csrf().disable() // Disable CSRF for simplicity
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/auth/**").permitAll() // Allow all /auth/** endpoints
                .anyExchange().authenticated() // Secure all other endpoints
            )
            .build();
    }
}
