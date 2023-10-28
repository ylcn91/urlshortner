package com.doksanbir.urlshortner.urlshortenerservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUrlListedEvent {
    private String userId;
    private List<UrlMapping> urlMappings;
}
