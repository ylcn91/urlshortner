package com.doksanbir.urlshortner.urlshortenerservice.usecases;


import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.CreateShortUrlUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateShortUrlUseCase implements CreateShortUrlUseCasePort {

    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlShortenerKafkaAdapter kafkaAdapter;
    private final UrlMappingCacheAdapter cacheAdapter;

    @Override
    public String createShortUrl(String longUrl, String userId, String customAlias,
                                 LocalDateTime expirationDate, List<String> tags) throws NoSuchAlgorithmException {

        validateLongUrl(longUrl);
        String shortUrl = generateShortUrl(longUrl);

        UrlMapping urlMapping = createUrlMapping(longUrl, userId, customAlias, expirationDate, tags, shortUrl);
        persistUrlMapping(urlMapping);
        cacheUrlMapping(shortUrl, urlMapping);
        publishUrlCreatedEvent(urlMapping);

        return shortUrl;
    }

    private void validateLongUrl(String longUrl) {
        if (longUrl == null || longUrl.isEmpty()) {
            throw new IllegalArgumentException("Long URL cannot be empty");
        }
    }

    private String generateShortUrl(String longUrl) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(longUrl.getBytes());

        // Convert byte array into a Base64 string representation
        String base64Hash = Base64.getEncoder().encodeToString(hash);

        // Take the first 8 characters as the short URL
        return base64Hash.substring(0, 8);
    }

    private UrlMapping createUrlMapping(String longUrl, String userId, String customAlias,
                                        LocalDateTime expirationDate, List<String> tags, String shortUrl) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setId(UUID.randomUUID());
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setLongUrl(longUrl);
        urlMapping.setUserId(userId);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpirationDate(expirationDate);
        urlMapping.setActive(true);
        urlMapping.setCustomAlias(customAlias);
        urlMapping.setTags(tags);
        return urlMapping;
    }

    private void persistUrlMapping(UrlMapping urlMapping) {
        urlMappingRepository.save(urlMapping);
    }

    private void cacheUrlMapping(String shortUrl, UrlMapping urlMapping) {
        cacheAdapter.cacheUrlMapping(shortUrl, urlMapping);
    }

    private void publishUrlCreatedEvent(UrlMapping urlMapping) {
        kafkaAdapter.publishUrlCreatedEvent(urlMapping);
    }


}

