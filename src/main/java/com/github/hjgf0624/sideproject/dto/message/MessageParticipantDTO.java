package com.github.hjgf0624.sideproject.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hjgf0624.sideproject.dto.user.UserProfileDTO;
import com.github.hjgf0624.sideproject.entity.ParticipantTypeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "메세지 참여자 DTO")
public class MessageParticipantDTO {
    @Schema(description = "참여자 유형 (발행자 or 참여자)")
    @JsonProperty("participant_type")
    private ParticipantTypeEntity participantType;

    @Schema(description = "사용자 프로필")
    @JsonProperty("profile")
    private UserProfileDTO profile;
}
