package com.github.hjgf0624.sideproject.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사용자 응답 DTO")
public class UserRegisterResponseDTO {

    @Schema(description = "생성된 사용자 고유 ID")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Firebase 토큰")
    @JsonProperty("firebase_token")
    private String firebaseToken;

    @Schema(description = "사용자 생년월일 (YYYY-MM-DD 형식)")
    @JsonProperty("birth_date")
    private String birthDate;

    @Schema(description = "프로필 정보")
    @JsonProperty("profile")
    private ProfileDTO profile;

    @Schema(description = "위치 정보")
    @JsonProperty("location")
    private LocationDTO location;

    @Schema(description = "계정 생성 시각 (ISO-8601 형식)")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "사용자 프로필 DTO")
    public static class ProfileDTO {

        @Schema(description = "사용자 이름")
        @JsonProperty("name")
        private String name;

        @Schema(description = "사용자 닉네임")
        @JsonProperty("nickname")
        private String nickname;

        @Schema(description = "프로필 이미지 URL")
        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @Schema(description = "전화번호")
        @JsonProperty("phone_number")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "위치 정보 DTO")
    public static class LocationDTO {

        @Schema(description = "사용자의 위도")
        @JsonProperty("latitude")
        private double latitude;

        @Schema(description = "사용자의 경도")
        @JsonProperty("longitude")
        private double longitude;
    }
}