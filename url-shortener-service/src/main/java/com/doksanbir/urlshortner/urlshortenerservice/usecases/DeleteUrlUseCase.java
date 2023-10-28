package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.DeleteUrlUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteUrlUseCase implements DeleteUrlUseCasePort {
    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlMappingCacheAdapter cacheAdapter;
    private final UrlShortenerKafkaAdapter kafkaAdapter;

    @Override
    public void deleteUrl(String shortUrl) {
        UrlMapping existingMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (existingMapping == null) {
            log.warn("Short URL not found: {}", shortUrl);
            kafkaAdapter.publishUrlNotFoundEvent(shortUrl);
            return;
        }
        urlMappingRepository.delete(shortUrl);
        cacheAdapter.removeCachedUrlMapping(shortUrl);
        kafkaAdapter.publishUrlDeletedEvent(shortUrl);
        log.info("Deleted URL mapping: {}", existingMapping);
    }
}


