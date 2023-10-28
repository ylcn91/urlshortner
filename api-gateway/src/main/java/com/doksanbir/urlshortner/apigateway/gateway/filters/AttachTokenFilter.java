package com.doksanbir.urlshortner.apigateway.gateway.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class AttachTokenFilter implements GlobalFilter, Ordered {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.defer(() -> {
            if (exchange.getResponse().getStatusCode() == HttpStatus.CREATED) {
                String username = getUsernameFromResponse(exchange);
                if (username != null) {
                    return reactiveRedisTemplate.opsForValue().get(username)
                            .flatMap(token -> {
                                if (token != null) {
                                    exchange.getResponse().getHeaders().add("Authorization", "Bearer " + token);
                                }
                                return Mono.empty();
                            });
                }
            }
            return Mono.empty();
        }));
    }


    private String getUsernameFromResponse(ServerWebExchange exchange) {

        return exchange.getResponse().getHeaders().getFirst("X-Username");
    }

    @Override
    public int getOrder() {
        return -1; // This should run as one of the last filters.
    }
}
