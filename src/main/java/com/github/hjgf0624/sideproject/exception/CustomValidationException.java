package com.github.hjgf0624.sideproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomValidationException extends RuntimeException{
    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors, String message) {
        super(message);
        this.errors = errors;
    }

    public CustomValidationException(String message) {
        super(message);
        this.errors = null;
    }
}
