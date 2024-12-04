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
        http.csrf().disable()
            .authorizeExchange()
            .pathMatchers("/api/users/register").permitAll()  // Ensure this matches the actual route
            .anyExchange().authenticated()
            .and()
            .httpBasic(); // or any other authentication method you need
        return http.build();
    }
}
