package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.dto.alarm.AlarmRequest;
import com.github.hjgf0624.sideproject.dto.alarm.AlarmResponse;
import com.github.hjgf0624.sideproject.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/getAlarmList")
    public ResponseEntity<List<AlarmResponse>> getAlarmList(@RequestBody AlarmRequest request) {
        List<AlarmResponse> alarms = alarmService.getAlarmList(request.getUserId(), request.getLocation());
        return ResponseEntity.ok(alarms);
    }
}
