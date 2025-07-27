package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "사용자 ID 찾기 응답 DTO")
public class UserFindIdResponseDTO {

    @Schema(
            description = "사용자 ID",
            example = "11@11.com"
    )
    private String email;

    @Schema(
            description = "사용자 ID 생성날짜",
            example = "2025-01-01"
    )
    private String created_At_id;
}
