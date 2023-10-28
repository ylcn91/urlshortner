package com.doksanbir.urlshortner.userservice.application.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Object id) {
        super("User not found: " + id);
    }
}


