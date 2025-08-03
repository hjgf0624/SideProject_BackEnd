package com.github.hjgf0624.sideproject.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 패스워드 리셋 요청 DTO")
public class UserPwdResetDTO {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(description = "새 비밀번호", example = "newPassword123")
    @NotBlank(message = "새 비밀번호는 필수입니다.")
    private String newPwd;

    @Schema(description = "비밀번호 확인", example = "newPassword123")
    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPwd;

    @Schema(description = "인증코드", example = "123456")
    @NotBlank(message = "인증 코드는 필수입니다.")
    private String verificationCode;

}
