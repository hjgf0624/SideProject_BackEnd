package com.github.hjgf0624.sideproject.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 프로필 업데이트 요청 DTO")
@JsonPropertyOrder({"user_id", "name", "nickname", "profile_image_url", "phone_number"})
public class UserProfileUpdateDTO extends UserProfileDTO {

    @Schema(description = "사용자가 입력한 이메일", example = "test@example.com")
    @JsonProperty("user_id")
    private String userId;
}
