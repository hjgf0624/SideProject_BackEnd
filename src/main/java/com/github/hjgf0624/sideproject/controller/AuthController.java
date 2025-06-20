package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.JwtTokenProvider;
import com.github.hjgf0624.sideproject.dto.*;
import com.github.hjgf0624.sideproject.dto.user.UserLoginDTO;
import com.github.hjgf0624.sideproject.dto.user.UserLoginResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserRegisterDTO;
import com.github.hjgf0624.sideproject.dto.user.UserRegisterResponseDTO;
import com.github.hjgf0624.sideproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.github.hjgf0624.sideproject.service.EmailAuthService;
import com.github.hjgf0624.sideproject.service.SmsAuthService;
import lombok.RequiredArgsConstructor;


import java.util.Collections;
import java.util.Map;

import java.io.IOException;

@Tag(name = "auth", description = "보안과 관련된 API 입니다.")
@RestController
@RequestMapping("/api/auth")
//@AllArgsConstructor
@RequiredArgsConstructor
public class AuthController {
  
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDTO<UserRegisterResponseDTO>> register(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImg,
            @RequestPart(value = "userRegisterDTO") UserRegisterDTO userRegisterDTO
    ) throws IOException {
        BaseResponseDTO<UserRegisterResponseDTO> dto = userService.register(userRegisterDTO, profileImg);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<UserLoginResponseDTO>> login(@RequestBody UserLoginDTO userLoginDTO) {
        BaseResponseDTO<UserLoginResponseDTO> dto = userService.login(userLoginDTO);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "사용자 위치 저장", description = "사용자 위치를 서버에 저장합니다.")
    @PostMapping("/updateLocation")
    public ResponseEntity<BaseResponseDTO<String>> updateLocation(@RequestBody UpdateLocationDTO locationDTO) {
        return ResponseEntity.ok(userService.updateLocation(locationDTO));
    }

    @Operation(summary = "사용자 프로필 정보 불러오기", description = "사용자 프로필을 서버에서 불러옵니다.")
    @PostMapping("/getProfileInfo")
    public ResponseEntity<BaseResponseDTO<UserProfileDTO>> getProfile(@RequestBody String userId) {
        return ResponseEntity.ok(userService.getProfileInfo(userId));
    }

    @Operation(summary = "사용자 프로필 정보 업데이트", description = "사용자 프로필을 업데이트 합니다.")
    @PostMapping(value = "/updateProfileInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDTO<UserProfileDTO>> updateProfile(
            @RequestPart(value = "userProfileUpdateDTO") UserProfileUpdateDTO userProfileUpdateDTO,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) throws IOException {
        return ResponseEntity.ok(userService.updateProfileInfo(userProfileUpdateDTO, profileImg));
    }


    @Operation(summary = "토큰 만료 검증 및 재발급", description = "현재 사용자의 토큰 만료 및 재발급 에 대한 검증을 진행합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponseDTO<UserLoginResponseDTO>> refresh(@RequestBody ReIssueTokenDTO reIssueTokenDTO) {
        System.out.println(reIssueTokenDTO);
        return ResponseEntity.ok(userService.refreshAccessToken(reIssueTokenDTO));
    }

    @Operation(summary = "로그아웃", description = "현재 사용자의 로그아웃을 진행합니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody UserLogoutDTO dto) {
        return ResponseEntity.ok(userService.logout(dto));
    }
  
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

    @Operation(summary = "FCM 토큰 저장 / 갱신")
    @PostMapping("/fcmTokenSaveOrRefresh")
    public ResponseEntity<BaseResponseDTO<String>> saveOrUpdateFcmToken(@RequestBody FcmTokenRequestDTO fcmTokenRequestDTO) {
        return ResponseEntity.ok(userService.saveOrUpdateFcmToken(fcmTokenRequestDTO.getUserId(), fcmTokenRequestDTO.getFcmToken()));
    }

}
