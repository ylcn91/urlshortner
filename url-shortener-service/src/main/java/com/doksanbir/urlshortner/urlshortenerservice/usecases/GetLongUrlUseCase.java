package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.GetLongUrlUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetLongUrlUseCase implements GetLongUrlUseCasePort {
    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlShortenerKafkaAdapter kafkaAdapter;
    private final UrlMappingCacheAdapter cacheAdapter;

    @Override
    public String getLongUrl(String shortUrl) {
        UrlMapping existingMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (existingMapping != null) {
            log.info("Found long URL: {}", existingMapping.getLongUrl());
            kafkaAdapter.publishUrlRetrievedEvent(existingMapping);
            cacheAdapter.cacheUrlMapping(shortUrl, existingMapping);
            return existingMapping.getLongUrl();
        } else {
            log.warn("Short URL not found: {}", shortUrl);
            kafkaAdapter.publishUrlNotFoundEvent(shortUrl);
            throw new IllegalArgumentException("Short URL not found");
        }
    }
}


