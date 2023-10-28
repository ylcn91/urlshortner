package com.doksanbir.urlshortner.userservice.infrastructure;

import com.doksanbir.urlshortner.userservice.application.ports.UserPort;
import com.doksanbir.urlshortner.userservice.application.usecases.*;
import com.doksanbir.urlshortner.userservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserPortImpl implements UserPort {

    // TODO: Consider breaking down this class into more focused ports as the number of use-cases grows.
    // TODO: Evaluate the need for additional methods for future use-cases like 'ListUserURLs', 'UserAnalytics', etc.
    // TODO: If the class grows too large, consider using Controller-level wiring for use-cases to maintain Single Responsibility Principle.
    // TODO: Regularly update this class when new use-cases are added or existing ones are modified to ensure it stays current.

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;

    @Override
    public ResponseEntity<User> createUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        User savedUser = createUserUseCase.createUser(user, headers);
        return ResponseEntity.ok().headers(headers).body(savedUser);
    }



    @Override
    public User updateUser(Long id, User updatedUser) {
        return updateUserUseCase.updateUser(id, updatedUser);
    }

    @Override
    public User authenticateUser(String username, String rawPassword) {
        return authenticateUserUseCase.authenticate(username, rawPassword);
    }

    @Override
    public void deleteUser(Long id) {
        deleteUserUseCase.deleteUser(id);
    }

    @Override
    public void deactivateUser(Long id) {
        deactivateUserUseCase.deactivateUser(id);
    }
}

