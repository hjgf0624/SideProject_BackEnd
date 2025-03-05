package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.dto.LocationDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmRequest {
    private String userId;   // 메시지를 작성한 사용자 ID
    private LocationDto location; // 위치 정보
}
