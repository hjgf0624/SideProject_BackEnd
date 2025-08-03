package com.github.hjgf0624.sideproject.dto.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MsgDetailRequestDTO {

    @Schema(description = "생성된 메시지의 고유 ID")
    @JsonProperty("message_id")
    @NotNull(message = "메시지 ID는 필수입니다.")
    @Positive(message = "메시지 ID는 양수여야 합니다.")
    private Long messageId;  // 메시지 고유 ID
}
