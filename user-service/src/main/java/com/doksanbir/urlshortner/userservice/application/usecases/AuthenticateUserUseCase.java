package com.doksanbir.urlshortner.userservice.application.usecases;

import com.doksanbir.urlshortner.userservice.application.exception.AuthenticationFailedException;
import com.doksanbir.urlshortner.userservice.application.exception.UserNotFoundException;
import com.doksanbir.urlshortner.userservice.domain.model.User;
import com.doksanbir.urlshortner.userservice.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        if(passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        } else {
            throw new AuthenticationFailedException();
        }
    }
}

