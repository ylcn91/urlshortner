package com.doksanbir.urlshortner.jwtservice.service;

import com.doksanbir.urlshortner.jwtservice.configuration.JwtConfig;
import com.doksanbir.urlshortner.jwtservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;
    private final JwtUtil jwtUtil;

    @Override
    public String generateToken(String username) {
        log.info("Generating token for user: {}", username);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        log.info("Validating token: {}", token);
        return jwtUtil.getClaimFromToken(token, Claims::getSubject) != null;
    }

    @Override
    public String extractUsername(String token) {
        log.info("Extracting username from token: {}", token);
        return jwtUtil.getClaimFromToken(token, Claims::getSubject);
    }
}
