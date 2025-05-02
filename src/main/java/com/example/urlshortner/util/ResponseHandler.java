package com.example.urlshortner.util;

import com.example.urlshortner.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

public class ResponseHandler {
    public static <T> ResponseEntity<ApiResponse<T>> success(
            String message,
            T data,
            HttpStatus status,
            String path) {

        return new ResponseEntity<>(
                ApiResponse.<T>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .message(message)
                        .data(data)
                        .path(path)
                        .build(),
                status
        );
    }

    public static ResponseEntity<ApiResponse<?>> error(
            String errorMessage,
            HttpStatus status,
            String path) {

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .error(errorMessage)
                        .path(path)
                        .build(),
                status
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> create(
            String message,
            T data,
            String path) {

        return success(message, data, HttpStatus.CREATED, path);
    }
}