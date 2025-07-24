package com.github.hjgf0624.sideproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "이메일 인증번호 요청 DTO")
public class EmailAuthDTO {

    @Schema(
            description = "인증번호 요청할 이메일",
            example = "11@11.com"
    )
    private String email;
}