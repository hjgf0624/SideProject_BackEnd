package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 패스워드 리셋 요청 DTO")
public class UserPwdResetDTO {

    @Schema(
            description = "사용자 이메일",
            example = "test@example.com"
    )
    private String email;

    @Schema(
            description = "새 비밀번호",
            example = "newPassword123"
    )
    private String newPwd;

    @Schema(
            description = "비밀번호 확인",
            example = "newPassword123"
    )
    private String confirmPwd;

    @Schema(
            description = "인증코드",
            example = "123456"
    )
    private String verificationCode;
}
