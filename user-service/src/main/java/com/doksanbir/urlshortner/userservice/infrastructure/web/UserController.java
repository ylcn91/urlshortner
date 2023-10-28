package com.doksanbir.urlshortner.userservice.infrastructure.web;

import com.doksanbir.urlshortner.userservice.application.ports.UserPort;
import com.doksanbir.urlshortner.userservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserPort userPort;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userPort.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userPort.updateUser(id, user);
    }

    @PostMapping("/authenticate")
    public User authenticateUser(@RequestParam String username, @RequestParam String password) {
        return userPort.authenticateUser(username, password);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userPort.deleteUser(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable Long id) {
        userPort.deactivateUser(id);
    }
}

