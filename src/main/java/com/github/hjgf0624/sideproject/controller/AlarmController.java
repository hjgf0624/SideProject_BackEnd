package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.domain.CustomUser;
import com.github.hjgf0624.sideproject.dto.alarm.AlarmRequestDTO;
import com.github.hjgf0624.sideproject.dto.alarm.AlarmResponseDTO;
import com.github.hjgf0624.sideproject.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/getAlarmList")
    @Operation(summary = "주변 알람 리스트 조회", responses = {
            @ApiResponse(responseCode = "200", description = "알람 리스트 반환",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AlarmResponseDTO.class))))
    })
    public ResponseEntity<List<AlarmResponseDTO>> getAlarmList(@AuthenticationPrincipal CustomUser user) {
        String userId = user.getUserId();

        List<AlarmResponseDTO> alarms = alarmService.getAlarmList(userId);
        return ResponseEntity.ok(alarms);
    }
}
