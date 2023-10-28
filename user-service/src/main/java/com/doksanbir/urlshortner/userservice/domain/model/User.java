package com.doksanbir.urlshortner.userservice.domain.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @ToString.Exclude // Exclude from toString for security
    private String password;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean active = true;
}

