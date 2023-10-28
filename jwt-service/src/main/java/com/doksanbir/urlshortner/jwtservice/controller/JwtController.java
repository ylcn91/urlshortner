package com.doksanbir.urlshortner.jwtservice.controller;

import com.doksanbir.urlshortner.jwtservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JwtService jwtService;

    @GetMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable String token) {
        boolean isValid = jwtService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
