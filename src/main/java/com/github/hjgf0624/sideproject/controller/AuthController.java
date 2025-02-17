package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.dto.EmailAuthRequest;
import com.github.hjgf0624.sideproject.dto.PhoneAuthRequest;
import com.github.hjgf0624.sideproject.service.EmailAuthService;
import com.github.hjgf0624.sideproject.service.SmsAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;

    @PostMapping("/emailAuth")
    public ResponseEntity<Map<String, String>> emailAuth(@RequestBody EmailAuthRequest request) {
        String code = emailAuthService.sendEmailAuthCode(request.getEmail());
        return ResponseEntity.ok(Collections.singletonMap("code", code)); // 프론트에서 관리
    }

    @PostMapping("/phoneAuth")
    public ResponseEntity<Map<String, String>> phoneAuth(@RequestBody PhoneAuthRequest request) {
        String code = smsAuthService.sendSmsAuthCode(request.getPhone());
        return ResponseEntity.ok(Collections.singletonMap("code", code)); // 프론트에서 관리
    }
}
