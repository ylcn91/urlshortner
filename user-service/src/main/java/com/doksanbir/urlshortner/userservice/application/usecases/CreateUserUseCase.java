package com.doksanbir.urlshortner.userservice.application.usecases;


import com.doksanbir.urlshortner.userservice.domain.model.User;
import com.doksanbir.urlshortner.userservice.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, User> kafkaTemplate;

    @Value("${spring.topics.user-created}")
    private String userCreatedTopic;

    public User createUser(User user, HttpHeaders headers) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        String correlationId = UUID.randomUUID().toString();
        headers.add("X-UserId", savedUser.getId().toString());
        headers.add("X-Username", savedUser.getUsername());
        headers.add("X-Correlation-Id", correlationId);

        kafkaTemplate.send(userCreatedTopic, savedUser);

        return savedUser;
    }
}

