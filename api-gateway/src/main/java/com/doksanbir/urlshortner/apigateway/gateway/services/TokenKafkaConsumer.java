package com.doksanbir.urlshortner.apigateway.gateway.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TokenKafkaConsumer {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ObjectMapper objectMapper;

    public TokenKafkaConsumer(ReactiveRedisTemplate<String, String> reactiveRedisTemplate,
                              ObjectMapper objectMapper) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "token_created", groupId = "api-gateway-group")
    public void handleTokenCreated(String tokenInfoJson) {
        try {
            Map<String, String> tokenInfo = objectMapper.readValue(tokenInfoJson, new TypeReference<>() {
            });
            String username = tokenInfo.get("username");
            String token = tokenInfo.get("token");

            reactiveRedisTemplate.opsForValue().set(username, token).subscribe();
        } catch (JsonProcessingException e) {
            log.error("An error occurred while processing the token JSON", e);
        }
    }
}
