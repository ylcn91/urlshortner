package com.doksanbir.urlshortner.apigateway.gateway.filters;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CircuitBreakerFilter implements GlobalFilter, Ordered {

    private final CircuitBreaker circuitBreaker;
    private final int order;

    public CircuitBreakerFilter(CircuitBreaker circuitBreaker, @Value("${filters.circuitBreaker.order:-2}") int order) {
        this.circuitBreaker = circuitBreaker;
        this.order = order;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}



