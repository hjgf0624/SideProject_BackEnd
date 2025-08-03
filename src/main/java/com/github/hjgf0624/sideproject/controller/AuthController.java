package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.domain.CustomUser;
import com.github.hjgf0624.sideproject.dto.*;
import com.github.hjgf0624.sideproject.dto.user.*;
import com.github.hjgf0624.sideproject.exception.CustomException;
import com.github.hjgf0624.sideproject.exception.ErrorCode;
import com.github.hjgf0624.sideproject.service.AuthService;
import com.github.hjgf0624.sideproject.service.EmailAuthService;
import com.github.hjgf0624.sideproject.service.SmsAuthService;
import com.github.hjgf0624.sideproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Tag(name = "auth", description = "보안과 관련된 API 입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDTO<UserRegisterResponseDTO>> register(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImg,
            @RequestPart(value = "userRegisterDTO") UserRegisterDTO userRegisterDTO) throws IOException {

        return ResponseEntity.ok(userService.register(userRegisterDTO, profileImg));
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호가 올바르지 않음", content = @Content(schema = @Schema(implementation = ErrorCode.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<UserLoginResponseDTO>> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }

    @Operation(summary = "위치 업데이트")
    @PostMapping("/updateLocation")
    public ResponseEntity<BaseResponseDTO<String>> updateLocation(@AuthenticationPrincipal CustomUser user,
                                                                  @RequestBody @Valid LocationDTO locationDTO) {
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);
        return ResponseEntity.ok(userService.updateLocation(user.getUserId(), locationDTO));
    }

    @Operation(summary = "프로필 조회")
    @PostMapping("/getProfileInfo")
    public ResponseEntity<BaseResponseDTO<UserProfileDTO>> getProfile(@AuthenticationPrincipal CustomUser user) {
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);
        return ResponseEntity.ok(userService.getProfileInfo(user.getUserId()));
    }

    @Operation(summary = "프로필 업데이트")
    @PostMapping(value = "/updateProfileInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDTO<UserProfileDTO>> updateProfile(
            @AuthenticationPrincipal CustomUser user,
            @RequestPart(value = "userProfileUpdateDTO") UserProfileDTO userProfileDTO,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) throws IOException {

        if (user == null) throw new CustomException(ErrorCode.AUTH_005);
        return ResponseEntity.ok(userService.updateProfileInfo(user.getUserId(), userProfileDTO, profileImg));
    }

    @Operation(summary = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(schema = @Schema(implementation = ErrorCode.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponseDTO<UserLoginResponseDTO>> refresh(@RequestBody @Valid ReIssueTokenDTO reIssueTokenDTO) {
        return ResponseEntity.ok(userService.refreshAccessToken(reIssueTokenDTO));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid UserLogoutDTO dto) {
        return ResponseEntity.ok(userService.logout(dto));
    }

    @Operation(summary = "이메일 인증번호 전송")
    @PostMapping("/emailAuth")
    public ResponseEntity<Map<String, String>> emailAuth(@RequestBody @Valid EmailAuthDTO request) {
        String code = emailAuthService.sendEmailAuthCode(request.getEmail());
        return ResponseEntity.ok(Collections.singletonMap("code", code));
    }

    @Operation(summary = "휴대폰 인증번호 전송")
    @PostMapping("/phoneAuth")
    public ResponseEntity<Map<String, String>> phoneAuth(@RequestBody @Valid PhoneAuthDTO request) {
        String code = smsAuthService.sendSmsAuthCode(request.getPhone());
        return ResponseEntity.ok(Collections.singletonMap("code", code));
    }

    @Operation(summary = "FCM 토큰 저장 또는 갱신")
    @PostMapping("/fcmTokenSaveOrRefresh")
    public ResponseEntity<BaseResponseDTO<String>> saveOrUpdateFcmToken(@AuthenticationPrincipal CustomUser user,
                                                                        @RequestBody @Valid FcmTokenAcqDTO dto) {
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);
        return ResponseEntity.ok(userService.saveOrUpdateFcmToken(user.getUserId(), dto.getFcmToken()));
    }

    @Operation(summary = "아이디 찾기")
    @PostMapping("/findId")
    public ResponseEntity<BaseResponseDTO<UserFindIdResponseDTO>> findId(@RequestBody @Valid UserFindIdDTO request) {
        return ResponseEntity.ok(authService.findUserId(request));
    }

    @Operation(summary = "비밀번호 재설정")
    @PostMapping("/resetPassword")
    public ResponseEntity<BaseResponseDTO<Void>> resetPassword(@RequestBody @Valid UserPwdResetDTO request) {
        return ResponseEntity.ok(authService.resetPwd(request));
    }

    @Operation(summary = "이메일 인증번호 전송")
    @PostMapping("/sendEmailAuthCode")
    public ResponseEntity<BaseResponseDTO<Void>> sendEmailAuthCode(@RequestBody @Valid EmailAuthDTO request) {
        return ResponseEntity.ok(authService.sendAuthCodeToEmail(request));
    }

    @Operation(summary = "휴대폰 인증번호 전송")
    @PostMapping("/sendPhoneAuthCode")
    public ResponseEntity<BaseResponseDTO<Void>> sendPhoneAuthCode(@RequestBody @Valid PhoneAuthDTO request) {
        return ResponseEntity.ok(authService.sendAuthCodeToPhone(request));
    }

    @Operation(summary = "회원탈퇴")
    @PostMapping("/deleteMembership")
    public ResponseEntity<String> deleteMembership(@RequestHeader("Authorization") String token,
                                                   @AuthenticationPrincipal CustomUser user) {
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);
        return ResponseEntity.ok(userService.deleteMemberShip(token.substring(7), user.getUserId()));
    }
}