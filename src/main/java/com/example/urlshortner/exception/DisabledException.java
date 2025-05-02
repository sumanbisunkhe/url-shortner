package com.example.urlshortner.exception;

public class DisabledException extends RuntimeException {
    public DisabledException(String message) {
        super(message);
    }
}
