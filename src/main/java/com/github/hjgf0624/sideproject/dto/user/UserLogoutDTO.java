package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 로그아웃 요청 DTO")
public class UserLogoutDTO {
    @Schema(
            description = "사용자 Access Token"
    )
    private String accessToken;
}
