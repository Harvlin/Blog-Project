package com.project.blog_be.controller;

import com.project.blog_be.domain.dto.response.AuthResponse;
import com.project.blog_be.domain.dto.request.LoginRequest;
import com.project.blog_be.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth/login")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String tokenValid = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValid)
                .expiredIn(86400)
                .build();
        return ResponseEntity.ok(authResponse);
    }

}
