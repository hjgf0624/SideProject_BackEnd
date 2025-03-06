package com.github.hjgf0624.sideproject.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "메시지 응답 DTO")
public class MessageResponseDTO {

    @Schema(description = "생성된 메시지의 고유 ID")
    @JsonProperty("message_id")
    private Integer messageId;

    @Schema(description = "메시지를 작성한 사용자 ID")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "메시지 제목")
    @JsonProperty("title")
    private String title;

    @Schema(description = "메시지 내용")
    @JsonProperty("content")
    private String content;

    @Schema(description = "약속 시간")
    @JsonProperty("meeting_date_time")
    @NotBlank(message = "meeting_date_time는 필수 값입니다.")
    private LocalDateTime meetingDateTime;

    @Schema(description = "익명 여부 (true: 익명, false: 공개)")
    @JsonProperty("anonymous")
    private boolean anonymous;

    @Schema(description = "모집 인원 (1 이상의 정수)")
    @JsonProperty("recruit_count")
    private int recruitCount;

    @Schema(description = "선택된 카테고리 리스트")
    @JsonProperty("categories")
    private List<String> categories;

    @Schema(description = "위치 정보")
    @JsonProperty("location")
    private LocationDTO location;

    @Schema(description = "메시지 생성 시각 (ISO-8601 형식)")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
