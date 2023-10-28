package com.doksanbir.urlshortner.userservice.application.usecases;

import com.doksanbir.urlshortner.userservice.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Long> kafkaTemplate;

    @Value("${spring.topics.user-deleted}")
    private String userDeletedTopic;
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
        kafkaTemplate.send(userDeletedTopic, id);
    }
}
