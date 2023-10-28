package com.doksanbir.urlshortner.jwtservice.util;

import com.doksanbir.urlshortner.jwtservice.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    /**
     * Get a claim from the JWT token.
     *
     * @param token the JWT token.
     * @param claimsResolver function to resolve the claim.
     * @param <T> the type of the claim.
     * @return the claim.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all claims from the JWT token.
     *
     * @param token the JWT token.
     * @return the claims.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token.replace(jwtConfig.getTokenPrefix(), ""))
                .getBody();
    }
}

