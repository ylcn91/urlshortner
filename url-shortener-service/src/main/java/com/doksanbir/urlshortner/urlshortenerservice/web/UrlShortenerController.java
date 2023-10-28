package com.doksanbir.urlshortner.urlshortenerservice.web;

import com.doksanbir.urlshortner.urlshortenerservice.domain.model.UrlMapping;
import com.doksanbir.urlshortner.urlshortenerservice.usecases.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final CreateShortUrlUseCase createShortUrlUseCase;
    private final GetLongUrlUseCase getLongUrlUseCase;
    private final ExtendUrlExpirationUseCase extendUrlExpirationUseCase;
    private final ListUserUrlsUseCase listUserUrlsUseCase;
    private final UpdateUrlMappingUseCase updateUrlMappingUseCase;
    private final RedirectToLongUrlUseCase redirectToLongUrlUseCase;
    private final DeleteUrlUseCase deleteUrlUseCase;
    private final DeactivateUrlUseCase deactivateUrlUseCase;
    private final HttpServletResponse httpServletResponse;


    @GetMapping("/{shortUrl}")
    public void redirectToLongUrl(@PathVariable String shortUrl) throws IOException {
        UrlMapping urlMapping = redirectToLongUrlUseCase.redirectToLongUrl(shortUrl);
        if (urlMapping != null && urlMapping.isActive()) {
            httpServletResponse.sendRedirect(urlMapping.getLongUrl());
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Short URL not found or not active");
        }
    }

    @PostMapping("/create")
    public String createShortUrl(@RequestParam String longUrl, @RequestParam String userId,
                                 @RequestParam(required = false) String customAlias,
                                 @RequestParam(required = false) LocalDateTime expirationDate,
                                 @RequestParam(required = false) List<String> tags) throws NoSuchAlgorithmException {
        return createShortUrlUseCase.createShortUrl(longUrl, userId, customAlias, expirationDate, tags);
    }

    @GetMapping("/longurl/{shortUrl}")
    public String getLongUrl(@PathVariable String shortUrl) {
        return getLongUrlUseCase.getLongUrl(shortUrl);
    }

    @PostMapping("/extend/{shortUrl}")
    public void extendUrlExpiration(@PathVariable String shortUrl, @RequestParam LocalDateTime newExpirationDate) {
        extendUrlExpirationUseCase.extendUrlExpiration(shortUrl, newExpirationDate);
    }

    @GetMapping("/list/{userId}")
    public List<UrlMapping> listUserUrls(@PathVariable String userId, @RequestParam int offset, @RequestParam int limit) {
        return listUserUrlsUseCase.listUserUrls(userId, offset, limit);
    }

    @PutMapping("/update/{shortUrl}")
    public UrlMapping updateUrlMapping(@PathVariable String shortUrl, @RequestBody UrlMapping newMapping) {
        return updateUrlMappingUseCase.updateUrlMapping(shortUrl, newMapping);
    }

    @DeleteMapping("/{shortUrl}")
    public void deleteUrl(@PathVariable String shortUrl) {
        deleteUrlUseCase.deleteUrl(shortUrl);
    }

    @PostMapping("/{shortUrl}/deactivate")
    public void deactivateUrl(@PathVariable String shortUrl) {
        deactivateUrlUseCase.deactivateUrl(shortUrl);
    }

}
