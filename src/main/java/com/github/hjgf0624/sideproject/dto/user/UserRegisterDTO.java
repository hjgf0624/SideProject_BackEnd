package com.github.hjgf0624.sideproject.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private String email;

    @Schema(description = "사용자가 입력한 비밀번호", example = "password123")
    private String password;

    @JsonProperty("confirm_password") // JSON에서는 "confirm_password"로 받음
    @Schema(description = "사용자가 재입력한 비밀번호", example = "password123")
    private String confirmPassword;

    @JsonProperty("fcmToken") // JSON에서는 "fcmToken"으로 받음
    @Schema(description = "FCM 토큰", example = "your-fcm-token")
    private String fcmToken;

    @JsonProperty("birth_date") // JSON에서는 "birth_date"로 받음
    @Schema(description = "생년월일", example = "1995-08-25")
    private String birthDate;

    @Schema(description = "프로필 정보")
    private UserProfileDTO profile;
}