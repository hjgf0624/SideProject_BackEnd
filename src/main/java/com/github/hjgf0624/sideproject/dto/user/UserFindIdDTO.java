package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 ID 찾기 요청 DTO")
public class UserFindIdDTO {

    @Schema(
            description = "휴대폰 번호",
            example = "010-1234-5678"
    )
    private String phoneNum;
}
