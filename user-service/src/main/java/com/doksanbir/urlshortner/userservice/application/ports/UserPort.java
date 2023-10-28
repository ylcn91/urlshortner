package com.doksanbir.urlshortner.userservice.application.ports;

import com.doksanbir.urlshortner.userservice.domain.model.User;
import org.springframework.http.ResponseEntity;

public interface UserPort {
    ResponseEntity<User> createUser(User user);
    User updateUser(Long id, User updatedUser);
    User authenticateUser(String username, String rawPassword);
    void deleteUser(Long id);
    void deactivateUser(Long id);
}
