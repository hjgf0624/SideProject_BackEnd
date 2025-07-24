package com.github.hjgf0624.sideproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "휴대폰 인증번호 요청 DTO")
public class PhoneAuthDTO {

    @Schema(
            description = "휴대폰 번호",
            example = "010-1234-5678"
    )
    private String phone;
}