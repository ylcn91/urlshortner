package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

public interface CreateShortUrlUseCasePort {
    String createShortUrl(String longUrl, String userId, String customAlias, LocalDateTime expirationDate, List<String> tags) throws NoSuchAlgorithmException;
}