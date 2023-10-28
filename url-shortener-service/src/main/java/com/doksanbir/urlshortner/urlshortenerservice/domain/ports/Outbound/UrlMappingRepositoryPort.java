package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;

import java.util.List;

// Outbound Ports
public interface UrlMappingRepositoryPort {
    UrlMapping save(UrlMapping urlMapping);
    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUserId(String userId, int offset, int limit);
    UrlMapping update(UrlMapping urlMapping);
    void delete(String shortUrl);
}



