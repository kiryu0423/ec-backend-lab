package com.example.ecdemo.auth.controller;

import com.example.ecdemo.auth.dto.LoginRequest;
import com.example.ecdemo.auth.dto.LoginResponse;
import com.example.ecdemo.auth.dto.LogoutRequest;
import com.example.ecdemo.auth.dto.RefreshTokenRequest;
import com.example.ecdemo.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public void logout(
            @Valid @RequestBody LogoutRequest request
    ) {
        authService.logout(request);
    }
}
