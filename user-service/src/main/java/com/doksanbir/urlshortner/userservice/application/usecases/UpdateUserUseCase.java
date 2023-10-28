package com.doksanbir.urlshortner.userservice.application.usecases;

import com.doksanbir.urlshortner.userservice.application.exception.UserNotFoundException;
import com.doksanbir.urlshortner.userservice.domain.model.User;
import com.doksanbir.urlshortner.userservice.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        return userRepository.save(existingUser);
    }
}

