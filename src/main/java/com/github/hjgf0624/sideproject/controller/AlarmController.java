package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.domain.CustomUser;
import com.github.hjgf0624.sideproject.dto.alarm.AlarmResponseDTO;
import com.github.hjgf0624.sideproject.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "alarm", description = "알람 관련 API")
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(
            summary = "주변 알람 리스트 조회",
            description = "현재 사용자 기준으로 참여할 수 있는 메시지를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "알람 리스트 반환 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AlarmResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "ALARM_001: 주변 알람을 불러오는 데 실패했습니다."
            )
    })
    @PostMapping("/getAlarmList")
    public ResponseEntity<List<AlarmResponseDTO>> getAlarmList(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUser user) {

        String userId = user.getUserId();
        List<AlarmResponseDTO> alarms = alarmService.getAlarmList(userId);
        return ResponseEntity.ok(alarms);
    }
}