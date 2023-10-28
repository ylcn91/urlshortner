package com.doksanbir.urlshortner.apigateway.gateway.config;

import com.doksanbir.urlshortner.apigateway.gateway.filters.CircuitBreakerGatewayFilterFactory;
import com.doksanbir.urlshortner.apigateway.gateway.filters.RateLimiterGatewayFilterFactory;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class GatewayRoutes {

    private final CircuitBreakerGatewayFilterFactory cbFactory;
    private final RateLimiterGatewayFilterFactory rlFactory;

    @Value("${resilience4j.circuitbreaker.instances.default.slidingWindowSize}")
    private int slidingWindowSize;

    @Value("${rateLimiter.capacity:10}")
    private int rateLimiterCapacity;

    @Value("${rateLimiter.refillTokens}")
    private int rateLimiterRefillTokens;

    @Value("${rateLimiter.timeoutDuration}")
    private int rateLimiterTimeoutDuration;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        CircuitBreakerConfig cbConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(slidingWindowSize)
                .build();

        RateLimiterConfig rlConfig = RateLimiterConfig.custom()
                .limitForPeriod(rateLimiterCapacity)
                .limitRefreshPeriod(Duration.ofSeconds(rateLimiterRefillTokens))
                .timeoutDuration(Duration.ofMillis(rateLimiterTimeoutDuration))
                .build();

        return builder.routes()
                .route("user_service_route", r -> r.path("/users/**")
                        .filters(f -> f.filter(cbFactory.apply(cbConfig))
                                .filter(rlFactory.apply(rlConfig)))
                        .uri("lb://user-service"))
                .build();
    }
}
