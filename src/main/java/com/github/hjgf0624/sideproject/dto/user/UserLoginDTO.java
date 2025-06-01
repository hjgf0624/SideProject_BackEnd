package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 로그인 요청 DTO")
public class UserLoginDTO {

    @Schema(
            description = "사용자 이메일",
            example = "test@example.com"
    )
    private String email;

    @Schema(
            description = "사용자 비밀번호",
            example = "password123"
    )
    private String password;

//    @Schema(
//            description = "사용자 FCM토큰",
//            example = "password123"
//    )
//    private String fcmToken;

}
