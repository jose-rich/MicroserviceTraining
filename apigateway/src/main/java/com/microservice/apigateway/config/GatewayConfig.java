package com.microservice.apigateway.config;

import com.microservice.apigateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes().route("AUTH-SERVICE", r -> r.path("/user/**").filters(f -> f.filter(filter)).uri("lb://AUTH-SERVICE"))
                /*.route("PATIENT-SERVICE", r -> r.path("/patient/**").filters(f -> f.filter(filter)).uri("lb://PATIENT-SERVICE"))
                .route("HOSPITAL-SERVICE", r -> r.path("/hospital/**").filters(f -> f.filter(filter)).uri("lb://HOSPITAL-SERVICE"))*/.build();
    }
}
