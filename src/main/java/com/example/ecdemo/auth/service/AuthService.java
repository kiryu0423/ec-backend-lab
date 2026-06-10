package com.example.ecdemo.auth.service;

import com.example.ecdemo.auth.dto.LoginRequest;
import com.example.ecdemo.auth.dto.LoginResponse;
import com.example.ecdemo.auth.dto.LogoutRequest;
import com.example.ecdemo.auth.dto.RefreshTokenRequest;
import com.example.ecdemo.auth.entity.RefreshToken;
import com.example.ecdemo.auth.repository.RefreshTokenRepository;
import com.example.ecdemo.common.security.JwtTokenProvider;
import com.example.ecdemo.user.entity.User;
import com.example.ecdemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getUsername(),
                user.getRole().name()
        );

        String refreshToken = UUID.randomUUID().toString();

        refreshTokenRepository.save(
                new RefreshToken(
                        user.getUsername(),
                        refreshToken,
                        LocalDateTime.now().plusDays(7)
                )
        );

        return LoginResponse.bearer(accessToken, refreshToken);
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadCredentialsException("Refresh token expired");
        }

        User user = userRepository.findByUsername(refreshToken.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        String newAccessToken = jwtTokenProvider.createToken(
                user.getUsername(),
                user.getRole().name()
        );

        return LoginResponse.bearer(
                newAccessToken,
                refreshToken.getToken()
        );
    }

    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenRepository.deleteByToken(request.refreshToken());
    }
}
