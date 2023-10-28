package com.doksanbir.urlshortner.urlshortenerservice.adapter;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingCachePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UrlMappingCacheAdapter implements UrlMappingCachePort {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOps;

    public UrlMappingCacheAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    @Override
    public void cacheUrlMapping(String shortUrl, UrlMapping urlMapping) {
        valueOps.set(shortUrl, urlMapping);
    }

    @Override
    public UrlMapping getCachedUrlMapping(String shortUrl) {
        return (UrlMapping) valueOps.get(shortUrl);
    }

    @Override
    public void removeCachedUrlMapping(String shortUrl) {
        redisTemplate.delete(shortUrl);
    }

    public void cacheUserUrls(String userId, List<UrlMapping> urlMappings) {
        valueOps.set("user:" + userId, urlMappings);
    }

    public List<UrlMapping> getCachedUserUrls(String userId) {
        return (List<UrlMapping>) valueOps.get("user:" + userId);
    }

    public void removeCachedUserUrls(String userId) {
        redisTemplate.delete("user:" + userId);
    }
}
