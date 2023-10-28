package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

public interface DeactivateUrlUseCasePort {
    void deactivateUrl(String shortUrl);
}

