package com.github.hjgf0624.sideproject.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.hjgf0624.sideproject.entity.SexEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "회원가입 요청 DTO")
@JsonPropertyOrder({"email", "password", "confirm_password", "fcmToken", "birth_date", "profile"})
public class UserRegisterDTO {

    @Schema(description = "사용자가 입력한 이메일", example = "test@example.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "사용자가 입력한 비밀번호", example = "password123")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @JsonProperty("confirm_password")
    @Schema(description = "사용자가 재입력한 비밀번호", example = "password123")
    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    @JsonProperty("fcmToken")
    @Schema(description = "FCM 토큰", example = "your-fcm-token")
    @NotBlank(message = "FCM 토큰은 필수입니다.")
    private String fcmToken;

    @JsonProperty("birth_date")
    @Schema(description = "생년월일", example = "1995-08-25")
    @NotBlank(message = "생년월일은 필수입니다.")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "생년월일 형식은 yyyy-MM-dd 이어야 합니다."
    )
    private String birthDate;

    private UserProfileDTO profile;
}