package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.UpdateUrlMappingUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateUrlMappingUseCase implements UpdateUrlMappingUseCasePort {
    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlMappingCacheAdapter cacheAdapter;
    private final UrlShortenerKafkaAdapter kafkaAdapter;

    @Override
    public UrlMapping updateUrlMapping(String shortUrl, UrlMapping newMapping) {
        UrlMapping existingMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (existingMapping == null) {
            log.warn("Short URL not found: {}", shortUrl);
            kafkaAdapter.publishUrlNotFoundEvent(shortUrl);
            throw new IllegalArgumentException("Short URL not found");
        }
        existingMapping.setLongUrl(newMapping.getLongUrl());
        existingMapping.setExpirationDate(newMapping.getExpirationDate());
        UrlMapping updatedMapping = urlMappingRepository.update(existingMapping);
        cacheAdapter.removeCachedUrlMapping(shortUrl);
        log.info("Updated URL mapping: {}", updatedMapping);
        kafkaAdapter.publishUrlUpdatedEvent(updatedMapping);
        return updatedMapping;
    }
}


