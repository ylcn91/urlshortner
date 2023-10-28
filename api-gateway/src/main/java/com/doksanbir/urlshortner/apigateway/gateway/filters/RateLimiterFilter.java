package com.doksanbir.urlshortner.apigateway.gateway.filters;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RateLimiterFilter implements GlobalFilter, Ordered {

    private final RateLimiter rateLimiter;
    private final int order;

    public RateLimiterFilter(RateLimiter rateLimiter, @Value("${filters.rateLimiter.order:-1}") int order) {
        this.rateLimiter = rateLimiter;
        this.order = order;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .transformDeferred(RateLimiterOperator.of(rateLimiter));
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}

