package com.example.urlshortner.service;

import com.example.urlshortner.dto.request.UrlRequest;
import com.example.urlshortner.dto.response.UrlResponse;

public interface UrlService {
    UrlResponse shortenUrl(UrlRequest urlRequest);
    String getOriginalUrl(String shortCode);
    UrlResponse getUrlStats(String shortCode);
}