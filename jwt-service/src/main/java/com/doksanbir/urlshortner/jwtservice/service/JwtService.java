package com.doksanbir.urlshortner.jwtservice.service;

public interface JwtService {
    /**
     * Generates a JWT token based on the provided user details.
     *
     * @param username The username for which the token will be generated.
     * @return The generated JWT token.
     */
    String generateToken(String username);

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    String extractUsername(String token);
}