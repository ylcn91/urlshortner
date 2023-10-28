package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

public interface DeleteUrlUseCasePort {
    void deleteUrl(String shortUrl);
}
