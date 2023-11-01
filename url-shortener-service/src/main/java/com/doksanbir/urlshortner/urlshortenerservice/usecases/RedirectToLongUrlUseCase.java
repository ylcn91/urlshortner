package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.RedirectToLongUrlUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedirectToLongUrlUseCase implements RedirectToLongUrlUseCasePort {

    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlShortenerKafkaAdapter kafkaAdapter;
    private final UrlMappingCacheAdapter cacheAdapter;

    @Override
    public UrlMapping redirectToLongUrl(String shortUrl) {
        UrlMapping urlMapping = cacheAdapter.getCachedUrlMapping(shortUrl);

        log.info("Retrieved URL mapping from cache: {}", urlMapping);

        if (urlMapping == null) {
            urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
            cacheAdapter.cacheUrlMapping(shortUrl, urlMapping);
        }

        if (urlMapping != null && urlMapping.isActive()) {
            kafkaAdapter.publishUrlAccessedEvent(urlMapping);
            log.info("Redirecting to long URL: {}", urlMapping.getLongUrl());
            return urlMapping;
        } else {
            log.warn("Short URL not found or not active: {}", shortUrl);
            return null;
        }
    }
}


