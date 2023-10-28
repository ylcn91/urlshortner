package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

import java.time.LocalDateTime;

public interface ExtendUrlExpirationUseCasePort {
    void extendUrlExpiration(String shortUrl, LocalDateTime newExpirationDate);
}
