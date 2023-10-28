package com.doksanbir.urlshortner.userservice.application.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("Authentication failed");
    }
}

