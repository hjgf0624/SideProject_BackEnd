package com.github.hjgf0624.sideproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "위치 정보 DTO")
public class LocationDTO {

    @NotNull(message = "latitude 값이 필요합니다.")
    @Schema(description = "위도")
    @JsonProperty("latitude")
    private Double latitude; // 위도

    @Schema(description = "경도")
    @JsonProperty("longitude")
    @NotNull(message = "longitude 값이 필요합니다.")
    private Double longitude; // 경도
}