package com.github.hjgf0624.sideproject.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "메시지 참석 요청 DTO")
public class JoinMessageDTO {

    @Schema(description = "생성된 메시지의 고유 ID")
    @JsonProperty("message_id")
    private Integer messageId;

    @Schema(description = "생성된 사용자 고유 ID")
    @JsonProperty("user_id")
    private String userId;

}
