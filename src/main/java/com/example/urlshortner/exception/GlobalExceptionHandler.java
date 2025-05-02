package com.example.urlshortner.exception;


import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseHandler.error(
                "Validation failed: " + errorMessage,
                HttpStatus.BAD_REQUEST,
                request.getDescription(false)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAllExceptions(
            Exception ex,
            WebRequest request) {

        return ResponseHandler.error(
                "Server Error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getDescription(false)
        );
    }
    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredUrl(
            UrlExpiredException ex,
            WebRequest request) {
        return ResponseHandler.error(
                ex.getMessage(),
                HttpStatus.GONE,
                request.getDescription(false)
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabledUrl(
            DisabledException ex,
            WebRequest request) {
        return ResponseHandler.error(
                ex.getMessage(),
                HttpStatus.FORBIDDEN,
                request.getDescription(false)
        );
    }
}