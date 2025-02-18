package com.github.hjgf0624.sideproject.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ValidationErrorResponseDTO {
    private final boolean success;
    private final String message;
    private final Map<String, String> errors;

    public static ValidationErrorResponseDTO of(Map<String, String> errors) {
        return ValidationErrorResponseDTO.builder()
                .success(false)
                .message("Validation Error")
                .errors(errors)
                .build();
    }
}
