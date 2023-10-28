package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.DeactivateUrlUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeactivateUrlUseCase implements DeactivateUrlUseCasePort {

    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlShortenerKafkaAdapter kafkaAdapter;
    private final UrlMappingCacheAdapter cacheAdapter;

    @Override
    public void deactivateUrl(String shortUrl) {
        UrlMapping existingMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (existingMapping != null) {
            existingMapping.setActive(false);
            urlMappingRepository.update(existingMapping);
            cacheAdapter.removeCachedUrlMapping(shortUrl);  // Invalidate cache
            kafkaAdapter.publishUrlDeactivatedEvent(shortUrl);  // Use a specific Kafka event for deactivation
            log.info("Deactivated URL: {}", existingMapping);
        } else {
            log.warn("Short URL not found: {}", shortUrl);
            kafkaAdapter.publishUrlNotFoundEvent(shortUrl);  // Kafka event for not found
        }
    }
}

