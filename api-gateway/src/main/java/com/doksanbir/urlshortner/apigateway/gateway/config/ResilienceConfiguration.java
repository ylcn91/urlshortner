package com.doksanbir.urlshortner.apigateway.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfiguration {

    @Bean
    public CircuitBreaker circuitBreaker() {
        return CircuitBreaker.ofDefaults("default");
    }

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.ofDefaults("default");
    }
}

