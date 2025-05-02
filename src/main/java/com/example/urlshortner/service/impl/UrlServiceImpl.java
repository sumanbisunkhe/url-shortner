package com.example.urlshortner.service.impl;

import com.example.urlshortner.dto.request.UrlRequest;
import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.dto.response.UrlResponse;
import com.example.urlshortner.exception.ShortCodeNotFoundException;
import com.example.urlshortner.exception.UrlExpiredException;
import com.example.urlshortner.mapper.UrlMapper;
import com.example.urlshortner.model.ShortUrl;
import com.example.urlshortner.repository.ShortUrlRepository;
import com.example.urlshortner.service.UrlService;
import com.example.urlshortner.util.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final ShortUrlRepository repository;
    private final UrlMapper mapper;


    @Override
    @Transactional
    public UrlResponse shortenUrl(UrlRequest urlRequest) {
        String originalUrl = urlRequest.getOriginalUrl();
        int validityDays = urlRequest.getValidityDays();

        validateUrl(originalUrl);

        String shortCode = generateShortCode(originalUrl);
        while (repository.existsByShortCode(shortCode)) {
            shortCode = generateShortCode(originalUrl + System.currentTimeMillis());
        }

        ShortUrl newUrl = ShortUrl.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .expirationDate(LocalDateTime.now().plusDays(validityDays))
                .build();

        return mapper.toResponse(repository.save(newUrl));
    }

    @Override
    @Transactional
    public String getOriginalUrl(String shortCode) {
        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortCodeNotFoundException("Short code not found"));

        if (LocalDateTime.now().isAfter(shortUrl.getExpirationDate())) {
            shortUrl.setActive(false);
            throw new UrlExpiredException("This URL has expired");
        }

        if (!shortUrl.isActive()) {
            throw new DisabledException("URL is disabled");
        }

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        return shortUrl.getOriginalUrl();
    }

    @Override
    public UrlResponse getUrlStats(String shortCode) {
        return mapper.toResponse(repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortCodeNotFoundException("Short code not found")));
    }

    private String generateShortCode(String originalUrl) {
        try {
            String saltedUrl = originalUrl + UUID.randomUUID();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(saltedUrl.getBytes(StandardCharsets.UTF_8));
            long hashLong = bytesToLong(hashBytes);
            return base62Encode(hashLong).substring(0, 8);
        } catch (Exception e) {
            throw new RuntimeException("Error generating short code", e);
        }
    }

    private long bytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (bytes[i] & 0xFF);
        }
        return value & Long.MAX_VALUE;
    }

    private String base62Encode(long number) {
        if (number == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.append(BASE62.charAt((int) (number % 62)));
            number /= 62;
        }
        return sb.reverse().toString();
    }

    private String regenerateShortCode(String originalCode) {
        return originalCode + BASE62.charAt(new Random().nextInt(BASE62.length()));
    }

    private void validateUrl(String url) {
        try {
            new URI(url).parseServerAuthority();
        } catch (URISyntaxException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}