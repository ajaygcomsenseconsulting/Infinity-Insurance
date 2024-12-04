package com.infinityinsurance.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public GlobalFilter jwtTokenFilter(WebClient.Builder webClientBuilder) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            
            // Exclude registration and other open paths from token validation
            if (path.startsWith("/api/users/register")) {
                return chain.filter(exchange);
            }

            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                String finalToken = token.substring(7);

                return webClientBuilder.build()
                        .post()
                        .uri("http://authentication-service/auth/validate")
                        .header("Authorization", "Bearer " + finalToken)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .then(chain.filter(exchange));
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("USER-SERVICE", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                .route("AUTHENTICATION-SERVICE", r -> r.path("/auth/**")
                        .uri("lb://authentication-service"))
                .build();
    }
}
