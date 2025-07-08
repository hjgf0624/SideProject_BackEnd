package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "알람 Request DTO")
public class AlarmRequestDTO {

    @Schema(description = "사용자 ID")
    private String userId;   // 메시지를 작성한 사용자 ID

    @Schema(description = "사용자 위치정보")
    private LocationDTO location; // 위치 정보
}
