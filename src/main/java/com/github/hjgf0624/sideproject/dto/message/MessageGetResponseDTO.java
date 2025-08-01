package com.github.hjgf0624.sideproject.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hjgf0624.sideproject.dto.CategorySimpleDTO;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "메시지 응답 DTO")
public class MessageGetResponseDTO {

    @Schema(description = "메세지 ID")
    @JsonProperty("message_id")
    private Long id;

    @Schema(description = "약속 날짜 (YYYY-MM-DD 형식)")
    @JsonProperty("date")
    private String date;

    @Schema(description = "약속 시간 (HH:mm 형식)")
    @JsonProperty("time")
    private String time;

    @Schema(description = "메시지 제목")
    @JsonProperty("title")
    private String title;

    @Schema(description = "메시지 세부 내용")
    @JsonProperty("contents")
    private String contents;

    @Schema(description = "모집 인원 정보")
    @JsonProperty("recruit-count")
    private RecruitCount recruitCount;

    @Schema(description = "위치 정보")
    @JsonProperty("location")
    private LocationDTO location;

    @Schema(description = "메세지 카테고리")
    @JsonProperty("category")
    private List<CategorySimpleDTO> category;

    @Schema(description = "참여자 정보")
    @JsonProperty("participant")
    private List<MessageParticipantDTO> memberList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "모집 인원 DTO")
    public static class RecruitCount {
        @Schema(description = "현재 모집된 인원 수")
        @JsonProperty("current")
        private int current;

        @Schema(description = "최대 모집 가능 인원 수")
        @JsonProperty("max")
        private int max;
    }
}