package com.example.urlshortner.controller;


import com.example.urlshortner.dto.request.UrlRequest;
import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.dto.response.UrlResponse;
import com.example.urlshortner.service.UrlService;
import com.example.urlshortner.util.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/urls")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<ApiResponse<UrlResponse>> shortenUrl(
            @RequestBody UrlRequest request,
            HttpServletRequest servletRequest) {

        // Pass the entire request object instead of just originalUrl
        UrlResponse response = urlService.shortenUrl(request);
        return ResponseHandler.create(
                "URL shortened successfully",
                response,
                servletRequest.getRequestURI()
        );
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ApiResponse<UrlResponse>> getUrlStats(
            @PathVariable String shortCode,
            HttpServletRequest servletRequest) {

        return ResponseHandler.success(
                "URL stats retrieved",
                urlService.getUrlStats(shortCode),
                HttpStatus.OK,
                servletRequest.getRequestURI()
        );
    }
}