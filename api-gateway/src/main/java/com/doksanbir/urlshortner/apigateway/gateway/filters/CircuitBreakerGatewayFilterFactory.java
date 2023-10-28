package com.doksanbir.urlshortner.apigateway.gateway.filters;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CircuitBreakerGatewayFilterFactory extends AbstractGatewayFilterFactory<CircuitBreakerConfig> {

    private final CircuitBreakerFilter circuitBreakerFilter;

    @Override
    public GatewayFilter apply(CircuitBreakerConfig config) {
        return circuitBreakerFilter::filter;
    }
}

