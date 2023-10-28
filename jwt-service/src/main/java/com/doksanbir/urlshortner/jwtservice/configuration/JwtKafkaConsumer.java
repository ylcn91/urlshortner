package com.doksanbir.urlshortner.jwtservice.configuration;

import com.doksanbir.urlshortner.jwtservice.model.UserDto;
import com.doksanbir.urlshortner.jwtservice.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtKafkaConsumer {

    private final JwtService jwtService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user_created", groupId = "jwt-service-group")
    public void handleUserCreated(UserDto userDto) throws JsonProcessingException {
        log.info("Received user created event for user: {}", userDto.getUsername());
        String token = jwtService.generateToken(userDto.getUsername());
        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("username", userDto.getUsername());
        tokenInfo.put("token", token);

        String tokenInfoJson = objectMapper.writeValueAsString(tokenInfo);

        kafkaTemplate.send("token_created", tokenInfoJson); 
    }

    @KafkaListener(topics = "validate_token", groupId = "jwt-service-group")
    public void handleValidateToken(String token) {
        log.info("Received token validation request for token: {}", token);
        boolean isValid = jwtService.validateToken(token);

        kafkaTemplate.send("token_validated", String.valueOf(isValid));
    }
}

