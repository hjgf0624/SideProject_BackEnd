package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "알람 Request DTO")
public class AlarmRequestDTO {

    @Schema(description = "사용자 ID")
    @NotBlank(message = "사용자 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userId;   // 메시지를 작성한 사용자 ID

    @Schema(description = "사용자 위치정보")
    private LocationDTO location; // 위치 정보
}
