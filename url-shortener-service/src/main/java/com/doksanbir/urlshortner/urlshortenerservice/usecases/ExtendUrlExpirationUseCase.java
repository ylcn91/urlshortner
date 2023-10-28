package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.ExtendUrlExpirationUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExtendUrlExpirationUseCase implements ExtendUrlExpirationUseCasePort {
    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlShortenerKafkaAdapter kafkaAdapter;
    private final UrlMappingCacheAdapter cacheAdapter;

    @Override
    public void extendUrlExpiration(String shortUrl, LocalDateTime newExpirationDate) {
        UrlMapping existingMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (existingMapping == null) {
            log.warn("Short URL not found: {}", shortUrl);
            kafkaAdapter.publishUrlNotFoundEvent(shortUrl);
            throw new IllegalArgumentException("Short URL not found");
        }
        existingMapping.setExpirationDate(newExpirationDate);
        urlMappingRepository.update(existingMapping);
        log.info("Extended expiration date for URL: {}", existingMapping);
        kafkaAdapter.publishUrlExtendedEvent(existingMapping);
        cacheAdapter.cacheUrlMapping(shortUrl, existingMapping);
    }
}

