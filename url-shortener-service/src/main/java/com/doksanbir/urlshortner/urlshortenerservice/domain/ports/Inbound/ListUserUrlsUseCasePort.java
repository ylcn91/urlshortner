package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;

import java.util.List;

public interface ListUserUrlsUseCasePort {
    List<UrlMapping> listUserUrls(String userId, int offset, int limit);
}

