package com.doksanbir.urlshortner.urlshortenerservice.adapter.repository;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Outbound.UrlMappingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UrlMappingRepository implements UrlMappingRepositoryPort {


    private final JpaUrlMappingRepository jpaUrlMappingRepository;

    @Override
    public UrlMapping save(UrlMapping urlMapping) {
        return jpaUrlMappingRepository.save(urlMapping);
    }

    @Override
    public UrlMapping findByShortUrl(String shortUrl) {
        Optional<UrlMapping> result = jpaUrlMappingRepository.findByShortUrl(shortUrl);
        return result.orElse(null);
    }

    @Override
    public List<UrlMapping> findByUserId(String userId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return jpaUrlMappingRepository.findByUserId(userId, pageable).getContent();
    }

    @Override
    public UrlMapping update(UrlMapping urlMapping) {
        return jpaUrlMappingRepository.save(urlMapping); // JPA's save method updates if ID exists
    }

    @Override
    public void delete(String shortUrl) {
        jpaUrlMappingRepository.deleteByShortUrl(shortUrl);
    }
}

