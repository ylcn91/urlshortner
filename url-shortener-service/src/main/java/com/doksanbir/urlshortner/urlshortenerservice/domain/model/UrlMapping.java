package com.doksanbir.urlshortner.urlshortenerservice.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String shortUrl;
    private String longUrl;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;
    private boolean isActive;
    private String customAlias;

    @ElementCollection
    private List<String> tags; // For categorization
}

