package com.doksanbir.urlshortner.urlshortenerservice.usecases;

import com.doksanbir.urlshortner.urlshortenerservice.adapter.UrlMappingCacheAdapter;
import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound.ListUserUrlsUseCasePort;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging.UrlShortenerKafkaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListUserUrlsUseCase implements ListUserUrlsUseCasePort {
    private final UrlMappingRepositoryPort urlMappingRepository;
    private final UrlShortenerKafkaAdapter kafkaAdapter;
    private final UrlMappingCacheAdapter cacheAdapter;

    @Override
    public List<UrlMapping> listUserUrls(String userId, int offset, int limit) {
        List<UrlMapping> userUrls = urlMappingRepository.findByUserId(userId, offset, limit);
        log.info("Found {} URLs for user ID {}", userUrls.size(), userId);
        kafkaAdapter.publishUserUrlsListedEvent(userId, userUrls);
        cacheAdapter.cacheUserUrls(userId, userUrls);
        return userUrls;
    }
}


