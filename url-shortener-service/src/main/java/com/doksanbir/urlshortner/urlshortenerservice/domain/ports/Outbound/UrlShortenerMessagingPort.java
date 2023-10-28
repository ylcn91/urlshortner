package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;

import java.util.List;


public interface UrlShortenerMessagingPort {
    void publishUrlCreatedEvent(UrlMapping urlMapping);
    void publishUrlDeactivatedEvent(String shortUrl);
    void publishUrlAccessedEvent(UrlMapping urlMapping);
    void publishUrlNotFoundEvent(String shortUrl);
    void publishUrlExtendedEvent(UrlMapping existingMapping);
    void publishUrlUpdatedEvent(UrlMapping updatedMapping);
    void publishUrlRetrievedEvent(UrlMapping retrievedMapping);
    void publishUserUrlsListedEvent(String userId, List<UrlMapping> urlMappings);
    void publishUrlDeletedEvent(String shortUrl);
}

