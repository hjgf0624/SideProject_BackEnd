package com.github.hjgf0624.sideproject.dto.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "메시지 요청 DTO")
public class MessageRequestDTO {

    @Schema(description = "메시지를 작성한 사용자 ID", example = "user_12345")
    @JsonProperty("user_id")
    @NotBlank(message = "user_id는 필수 값입니다.")
    private String userId;

    @Schema(description = "메시지 제목", example = "스터디 모집합니다!")
    @JsonProperty("title")
    @NotBlank(message = "title은 필수 값입니다.")
    private String title;

    @Schema(description = "메시지 내용", example = "같이 스터디 하실 분 구합니다.")
    @JsonProperty("content")
    @NotBlank(message = "content는 필수 값입니다.")
    private String content;

    @Schema(description = "약속 시간", example = "2024-05-16 16:40")
    @JsonProperty("meeting_date_time")
    @NotNull(message = "meeting_date_time는 필수 값입니다.")
    private LocalDateTime meetingDateTime;

    @Schema(description = "익명 여부 (true: 익명, false: 공개)", example = "true")
    @JsonProperty("anonymous")
    private boolean anonymous;

    @Schema(description = "모집 인원 (최소 1명 이상)", example = "3")
    @JsonProperty("recruit_count")
    @Min(value = 1, message = "recruit_count는 최소 1 이상이어야 합니다.")
    private int recruitCount;

    @Schema(description = "선택된 카테고리 리스트", example = "[ 1, 2 ]")
    @JsonProperty("categories")
    @NotEmpty(message = "categories는 최소 하나 이상 선택해야 합니다.")
    private List<Long> categories;

    @Schema(description = "위치 정보")
    @JsonProperty("location")
    @NotNull(message = "location 정보는 필수입니다.")
    private LocationDTO location;
}