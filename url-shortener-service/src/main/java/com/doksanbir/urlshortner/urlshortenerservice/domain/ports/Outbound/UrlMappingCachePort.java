package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;

import java.util.List;

public interface UrlMappingCachePort {
    void cacheUrlMapping(String shortUrl, UrlMapping urlMapping);
    UrlMapping getCachedUrlMapping(String shortUrl);
    void removeCachedUrlMapping(String shortUrl);

    void cacheUserUrls(String userId, List<UrlMapping> userUrls);
}