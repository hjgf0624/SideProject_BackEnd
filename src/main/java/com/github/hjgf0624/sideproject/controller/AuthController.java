package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.JwtTokenProvider;
import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserLoginDTO;
import com.github.hjgf0624.sideproject.dto.user.UserLoginResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserRegisterDTO;
import com.github.hjgf0624.sideproject.dto.user.UserRegisterResponseDTO;
import com.github.hjgf0624.sideproject.entity.RoleEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "auth", description = "보안과 관련된 API 입니다.")
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원 가입 실패")
    })
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDTO<UserRegisterResponseDTO>> register(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImg,
            @RequestPart(value = "userRegisterDTO") UserRegisterDTO userRegisterDTO
    ) throws IOException {
        BaseResponseDTO<UserRegisterResponseDTO> dto = userService.register(userRegisterDTO, profileImg);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<UserLoginResponseDTO>> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserEntity user = userService.findUserById(userLoginDTO.getEmail());

        List<String> sAuthList = new ArrayList<>();
        for (RoleEntity role : user.getRoles()) {
            sAuthList.add(role.getRoleName());
        }

        String token = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), sAuthList);
        BaseResponseDTO<UserLoginResponseDTO> dto = userService.login(userLoginDTO, token);

        return ResponseEntity.ok(dto);
    }

}
