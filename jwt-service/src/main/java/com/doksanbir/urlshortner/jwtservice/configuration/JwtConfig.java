package com.doksanbir.urlshortner.jwtservice.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.headerString}")
    private String headerString;
}

