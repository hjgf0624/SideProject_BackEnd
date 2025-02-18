package com.github.hjgf0624.sideproject.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationException extends RuntimeException{
    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors, String message) {
        super(message);
        this.errors = errors;
    }
}
