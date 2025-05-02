package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.AuthRequest;
import com.example.urlshortner.dto.request.RefreshTokenRequest;
import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.dto.response.AuthResponse;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.UserRepository;
import com.example.urlshortner.security.JwtTokenUtil;
import com.example.urlshortner.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getRole().name(),
                jwtTokenUtil.getAccessTokenExpiration(),
                "Bearer"
        );

        return ResponseHandler.success("Login successful", authResponse, HttpStatus.OK, "/auth/login");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtTokenUtil.extractUsername(refreshToken);

        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!jwtTokenUtil.validateToken(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtTokenUtil.generateAccessToken(user);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);

        AuthResponse authResponse = new AuthResponse(
                newAccessToken,
                newRefreshToken,
                user.getUsername(),
                user.getRole().name(),
                jwtTokenUtil.getAccessTokenExpiration(),
                "Bearer"
        );

        return ResponseHandler.success("Token refreshed successfully", authResponse, HttpStatus.OK, "/auth/refresh-token");
    }
}