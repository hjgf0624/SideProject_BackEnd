package com.github.hjgf0624.sideproject.exception;

import com.github.hjgf0624.sideproject.dto.ValidationErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponseDTO handleCustomValidationException(CustomValidationException e) {
        return ValidationErrorResponseDTO.of(e.getErrors());
    }

}
