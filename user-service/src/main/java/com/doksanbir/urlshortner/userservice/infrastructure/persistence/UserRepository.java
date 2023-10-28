package com.doksanbir.urlshortner.userservice.infrastructure.persistence;

import com.doksanbir.urlshortner.userservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
