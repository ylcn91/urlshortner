package com.doksanbir.urlshortner.urlshortenerservice.adapter.repository;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUrlMappingRepository extends JpaRepository<UrlMapping, UUID> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);
    Page<UrlMapping> findByUserId(String userId, Pageable pageable);
    void deleteByShortUrl(String shortUrl);
}
