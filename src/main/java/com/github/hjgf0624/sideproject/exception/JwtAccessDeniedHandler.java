package com.github.hjgf0624.sideproject.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hjgf0624.sideproject.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {

        ErrorCode errorCode = ErrorCode.AUTH_007; // "접근 권한이 없습니다."

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                403,
                errorCode.getCode(),
                errorCode.getMessage()
        );

        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
