package com.doksanbir.urlshortner.jwtservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password; // Encoded
    private LocalDateTime createdDate;
    private String role; // Enum converted to String
    private boolean active;
}
