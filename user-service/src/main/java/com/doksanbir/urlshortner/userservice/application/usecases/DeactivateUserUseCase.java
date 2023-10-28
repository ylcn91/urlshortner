package com.doksanbir.urlshortner.userservice.application.usecases;

import com.doksanbir.urlshortner.userservice.application.exception.UserNotFoundException;
import com.doksanbir.urlshortner.userservice.domain.model.User;
import com.doksanbir.urlshortner.userservice.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeactivateUserUseCase {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Long> kafkaTemplate;

    @Value("${spring.topics.user-deactivated}")
    private String userDeactivatedTopic;
    public void deactivateUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        userRepository.save(user);

        kafkaTemplate.send(userDeactivatedTopic, id);
    }
}

