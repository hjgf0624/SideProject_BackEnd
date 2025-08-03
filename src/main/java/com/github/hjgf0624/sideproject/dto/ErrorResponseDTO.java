package com.github.hjgf0624.sideproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {

    @Schema(description = "HTTP 상태 코드", example = "404")
    private int status;

    @Schema(description = "에러 코드", example = "MSG_001")
    private String code;

    @Schema(description = "에러 메시지", example = "메시지를 찾을 수 없습니다.")
    private String message;

    public ErrorResponseDTO(int Status, String code, String message) {
        this.status = Status;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponseDTO of(int status, String code,String message) {
        return new ErrorResponseDTO(status, code, message);
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
