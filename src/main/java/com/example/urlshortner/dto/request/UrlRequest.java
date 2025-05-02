package com.example.urlshortner.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UrlRequest {
    @NotBlank(message = "URL is required")
    private String originalUrl;

    @Min(value = 1, message = "Validity days must be at least 1")
    @Max(value = 365, message = "Validity days cannot exceed 365")
    private int validityDays = 30;
}