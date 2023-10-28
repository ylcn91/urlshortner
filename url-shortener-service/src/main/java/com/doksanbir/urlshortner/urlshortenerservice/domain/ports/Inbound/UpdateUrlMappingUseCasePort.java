package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;

public interface UpdateUrlMappingUseCasePort {
    UrlMapping updateUrlMapping(String shortUrl, UrlMapping newMapping);
}
