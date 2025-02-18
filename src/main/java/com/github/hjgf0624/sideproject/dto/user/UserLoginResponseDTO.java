package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Schema(description = "사용자 로그인 응답 DTO")
public class UserLoginResponseDTO {

    @Schema(
            description = "사용자 이름",
            example = "홍길동"
    )
    private String name;

    @Schema(
            description = "사용자 이메일",
            example = "test@example.com"
    )
    private String email;
}
