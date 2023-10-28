package com.doksanbir.urlshortner.apigateway.gateway.filters;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<RateLimiterConfig> {


    private final RateLimiterFilter rateLimiterFilter;

    @Override
    public GatewayFilter apply(RateLimiterConfig config) {
        return rateLimiterFilter::filter;
    }
}

