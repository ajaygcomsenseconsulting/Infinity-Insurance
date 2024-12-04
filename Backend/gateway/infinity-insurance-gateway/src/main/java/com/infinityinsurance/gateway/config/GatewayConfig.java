package com.infinityinsurance.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    // Configure routes
    @Bean
    public GlobalFilter jwtTokenFilter() {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                // Validate token (implement actual validation logic here)
                token = token.substring(7); // Remove "Bearer " prefix

                // For now, simply set the user context (authentication) based on token
                // UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("user", null, null);
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            return chain.filter(exchange);
        };
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/users/**") // Match correct path for user service
                        .uri("lb://USER-SERVICE")  // Load-balancing the request to the user service
                )
                .route("insurance-service", r -> r.path("/insurance/**")
                        .uri("lb://INSURANCE-SERVICE")
                )
                .route("quote-service", r -> r.path("/quote/**")
                        .uri("lb://QUOTE-SERVICE")
                )
                .build();
    }
}
