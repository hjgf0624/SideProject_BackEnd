package com.github.hjgf0624.sideproject.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hjgf0624.sideproject.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        ErrorCode errorCode = ErrorCode.AUTH_006; // "유효하지 않은 토큰입니다."

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
