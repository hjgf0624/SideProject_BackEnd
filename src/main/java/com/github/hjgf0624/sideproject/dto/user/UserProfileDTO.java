package com.github.hjgf0624.sideproject.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사용자 프로필 DTO")
public class UserProfileDTO {

    @Schema(description = "사용자 이름", example = "홍길동")
    @JsonProperty("name")
    private String name;

    @Schema(description = "사용자 닉네임", example = "길동이")
    @JsonProperty("nickname")
    private String nickname;

    @Schema(description = "프로필 이미지 URL")
    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Schema(description = "사용자 위치")
    private LocationDTO location;
}

