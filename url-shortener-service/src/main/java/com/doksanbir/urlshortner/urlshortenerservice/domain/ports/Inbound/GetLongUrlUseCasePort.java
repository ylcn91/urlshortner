package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

public interface GetLongUrlUseCasePort {
    String getLongUrl(String shortUrl);
}
