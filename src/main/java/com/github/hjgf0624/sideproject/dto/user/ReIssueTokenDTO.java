package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "사용자 토큰 DTO")
public class ReIssueTokenDTO {
    @Schema(
            description = "로그인 후 발급한 Access Token"
    )
    private String accessToken;

    @Schema(
            description = "로그인 후 발급한 Refresh Token"
    )
    private String refreshToken;
}
