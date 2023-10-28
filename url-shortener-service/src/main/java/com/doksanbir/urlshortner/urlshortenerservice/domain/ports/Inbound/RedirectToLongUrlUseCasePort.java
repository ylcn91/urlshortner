package com.doksanbir.urlshortner.urlshortenerservice.domain.ports.Inbound;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;

public interface RedirectToLongUrlUseCasePort {
    UrlMapping redirectToLongUrl(String shortUrl);
}
