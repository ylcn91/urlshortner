package com.doksanbir.urlshortner.urlshortenerservice.adapter.messaging;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UserUrlListedEvent;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlShortenerMessagingPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class UrlShortenerKafkaAdapter implements UrlShortenerMessagingPort {

    private static final String URL_CREATED_TOPIC = "urlCreatedTopic";
    private static final String URL_DEACTIVATED_TOPIC = "urlDeactivatedTopic";
    private static final String URL_ACCESSED_TOPIC = "urlAccessedTopic";
    private static final String URL_NOT_FOUND_TOPIC = "urlNotFoundTopic";
    private static final String URL_EXTENDED_TOPIC = "urlExtendedTopic";
    private static final String URL_UPDATED_TOPIC = "urlUpdatedTopic";
    private static final String URL_RETRIEVED_TOPIC = "urlRetrievedTopic";
    private static final String USER_URLS_LISTED_TOPIC = "userUrlsListedTopic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UrlShortenerKafkaAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void sendEvent(String topic, Object payload) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, payload);
        future.thenAccept(result -> log.info("Successfully sent message to topic {} with offset {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset()))
                .exceptionally(ex -> {
                    log.error("Failed to send message", ex);
                    return null;
                });
    }


    @Override
    public void publishUrlCreatedEvent(UrlMapping urlMapping) {
        log.info("Publishing URL Created Event for {}", urlMapping.getShortUrl());
        sendEvent(URL_CREATED_TOPIC, urlMapping);
    }

    @Override
    public void publishUrlDeactivatedEvent(String shortUrl) {
        log.info("Publishing URL Deactivated Event for {}", shortUrl);
        sendEvent(URL_DEACTIVATED_TOPIC, shortUrl);
    }

    @Override
    public void publishUrlAccessedEvent(UrlMapping urlMapping) {
        log.info("Publishing URL Accessed Event for {}", urlMapping.getShortUrl());
        sendEvent(URL_ACCESSED_TOPIC, urlMapping);
    }

    @Override
    public void publishUrlNotFoundEvent(String shortUrl) {
        log.warn("Publishing URL Not Found Event for {}", shortUrl);
        sendEvent(URL_NOT_FOUND_TOPIC, shortUrl);
    }

    @Override
    public void publishUrlExtendedEvent(UrlMapping existingMapping) {
        log.info("Publishing URL Extended Event for {}", existingMapping.getShortUrl());
        sendEvent(URL_EXTENDED_TOPIC, existingMapping);
    }

    @Override
    public void publishUrlUpdatedEvent(UrlMapping updatedMapping) {
        log.info("Publishing URL Updated Event for {}", updatedMapping.getShortUrl());
        sendEvent(URL_UPDATED_TOPIC, updatedMapping);
    }

    @Override
    public void publishUrlRetrievedEvent(UrlMapping retrievedMapping) {
        log.info("Publishing URL Retrieved Event for {}", retrievedMapping.getShortUrl());
        sendEvent(URL_RETRIEVED_TOPIC, retrievedMapping);
    }

    @Override
    public void publishUserUrlsListedEvent(String userId, List<UrlMapping> urlMappings) {
        log.info("Publishing User URLs Listed Event for user {}", userId);
        UserUrlListedEvent event = new UserUrlListedEvent(userId, urlMappings);
        sendEvent(USER_URLS_LISTED_TOPIC, event);
    }

    @Override
    public void publishUrlDeletedEvent(String shortUrl) {
        sendEvent("urlDeletedTopic", shortUrl);
    }


}
