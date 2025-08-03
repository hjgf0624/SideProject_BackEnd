package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.config.security.JwtTokenProvider;
import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import com.github.hjgf0624.sideproject.dto.user.*;
import com.github.hjgf0624.sideproject.entity.*;
import com.github.hjgf0624.sideproject.exception.CustomException;
import com.github.hjgf0624.sideproject.exception.ErrorCode;
import com.github.hjgf0624.sideproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AwsS3Service awsS3Service;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final UserFcmTokenService userFcmTokenService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String BIRTH_REGEX = "^(19|20)\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public UserEntity toEntity(UserRegisterDTO dto, MultipartFile file) throws IOException {
        String profileImageUrl = file != null ? awsS3Service.uploadFile(file, dto.getEmail()) : null;
        UserEntity userEntity = new UserEntity();

        userEntity.setUserId(dto.getEmail());
        userEntity.setUserPw(passwordEncoder.encode(dto.getPassword()));
        userEntity.setBirthdate(dto.getBirthDate());

        if (dto.getProfile() != null) {
            userEntity.setName(dto.getProfile().getName());
            userEntity.setNickname(dto.getProfile().getNickname());
            userEntity.setProfileImageUrl(profileImageUrl);
            userEntity.setSex(dto.getProfile().getSex());
            userEntity.setPhoneNumber(dto.getProfile().getPhoneNumber());
            userEntity.setBirthdate(dto.getProfile().getBirthdate());
            userEntity.setLatitude(dto.getProfile().getLocation().getLatitude());
            userEntity.setLongitude(dto.getProfile().getLocation().getLongitude());
        }

        return userEntity;
    }

    public String logout(UserLogoutDTO dto) {
        String accessToken = dto.getAccessToken();
        String userId = jwtTokenProvider.getUserPK(accessToken);
        refreshTokenRepository.deleteRefreshToken(userId);

        Date expirationDate = jwtTokenProvider.getExpiration(accessToken);
        long ttl = expirationDate.getTime() - System.currentTimeMillis();

        if (ttl > 0) {
            refreshTokenRepository.saveBlackList(accessToken, ttl);
            userFcmTokenService.deleteToken(userId);
        }

        return "success";
    }

    public BaseResponseDTO<UserLoginResponseDTO> refreshAccessToken(ReIssueTokenDTO dto) {
        String accessToken = dto.getAccessToken();
        String refreshToken = dto.getRefreshToken();
        String userId = jwtTokenProvider.getUserPK(accessToken);

        if (jwtTokenProvider.isBlacklisted(accessToken)) {
            throw new CustomException(ErrorCode.AUTH_006);
        }

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.AUTH_006);
        }

        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            throw new CustomException(ErrorCode.AUTH_006);
        }

        if (!isValidRefreshToken(userId, refreshToken)) {
            throw new CustomException(ErrorCode.AUTH_006);
        }

        UserEntity user = userRepository.findByUserId(userId);
        List<String> roles = user.getRoles().stream().map(RoleEntity::getRoleName).toList();

        String reIssuedAccessToken = jwtTokenProvider.createToken(user.getUserId(), roles);

        UserLoginResponseDTO response = UserLoginResponseDTO.builder()
                .email(user.getUserId())
                .name(user.getName())
                .build();

        return BaseResponseDTO.success(response, "user")
                .addField("accessToken", reIssuedAccessToken)
                .addField("refreshToken", refreshToken)
                .addField("message", "Login successful");
    }

    public boolean isValidRefreshToken(String userId, String refreshToken) {
        String saved = refreshTokenRepository.getRefreshToken(userId);
        return saved != null && saved.equals(refreshToken);
    }

    public BaseResponseDTO<UserLoginResponseDTO> login(UserLoginDTO dto) {
        UserEntity user = userRepository.findByUserId(dto.getEmail());

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getUserPw())) {
            throw new CustomException(ErrorCode.AUTH_001);
        }

        if (dto.getFcmToken() == null) {
            throw new CustomException(ErrorCode.AUTH_009);
        }

        userFcmTokenService.saveOrUpdateToken(dto.getEmail(), dto.getFcmToken());

        List<String> roles = user.getRoles().stream().map(RoleEntity::getRoleName).toList();
        String accessToken = jwtTokenProvider.createToken(user.getUserId(), roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        refreshTokenRepository.saveRefreshToken(user.getUserId(), refreshToken);

        UserLoginResponseDTO response = UserLoginResponseDTO.builder()
                .email(user.getUserId())
                .name(user.getName())
                .build();

        return BaseResponseDTO.success(response, "user")
                .addField("accessToken", accessToken)
                .addField("refreshToken", refreshToken)
                .addField("message", "Login successful");
    }

    @Transactional
    public BaseResponseDTO<UserRegisterResponseDTO> register(UserRegisterDTO dto, MultipartFile file) throws IOException {
        if (userRepository.findByUserId(dto.getEmail()) != null) {
            throw new CustomException(ErrorCode.AUTH_002);
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new CustomException(ErrorCode.AUTH_001);
        }

        if (!Pattern.matches(EMAIL_REGEX, dto.getEmail())) {
            throw new CustomException(ErrorCode.AUTH_001);
        }

        if (!Pattern.matches(BIRTH_REGEX, dto.getBirthDate())) {
            throw new CustomException(ErrorCode.AUTH_001);
        }

        UserEntity user = toEntity(dto, file);
        RoleEntity role = roleRepository.findByRoleName("USER");

        user.addRole(role);
        UserEntity saved = userRepository.save(user);

        UserRegisterResponseDTO response = UserRegisterResponseDTO.builder()
                .userId(saved.getUserId())
                .firebaseToken(dto.getFcmToken())
                .profile(UserProfileDTO.builder()
                        .name(saved.getName())
                        .nickname(saved.getNickname())
                        .profileImageUrl(saved.getProfileImageUrl())
                        .phoneNumber(saved.getPhoneNumber())
                        .sex(saved.getSex())
                        .birthdate(saved.getBirthdate())
                        .location(LocationDTO.builder()
                                .latitude(saved.getLatitude())
                                .longitude(saved.getLongitude())
                                .build())
                        .build())
                .createdAt(saved.getCreatedAt())
                .build();

        return BaseResponseDTO.success(response, "data").addField("message", "Registration successful");
    }

    @Transactional
    public BaseResponseDTO<String> updateLocation(String userId, LocationDTO dto) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);

        user.setLatitude(dto.getLatitude());
        user.setLongitude(dto.getLongitude());
        userRepository.save(user);

        return BaseResponseDTO.success("위치 정보 저장 성공.", "message");
    }

    public BaseResponseDTO<UserProfileDTO> getProfileInfo(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);

        UserProfileDTO dto = UserProfileDTO.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .phoneNumber(user.getPhoneNumber())
                .birthdate(user.getBirthdate())
                .sex(user.getSex())
                .location(LocationDTO.builder()
                        .latitude(user.getLatitude())
                        .longitude(user.getLongitude())
                        .build())
                .build();

        return BaseResponseDTO.success(dto, "user_profile").addField("message", "프로필 정보 불러오기 성공.");
    }

    @Transactional
    public BaseResponseDTO<UserProfileDTO> updateProfileInfo(String userId, UserProfileDTO dto, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);

        String profileImageUrl = file != null ? awsS3Service.uploadFile(file, userId) : user.getProfileImageUrl();

        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setBirthdate(dto.getBirthdate());
        user.setSex(dto.getSex());
        user.setProfileImageUrl(profileImageUrl);

        userRepository.save(user);

        UserProfileDTO updated = UserProfileDTO.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .phoneNumber(user.getPhoneNumber())
                .birthdate(user.getBirthdate())
                .sex(user.getSex())
                .build();

        return BaseResponseDTO.success(updated, "user_profile").addField("message", "프로필 정보 업데이트 성공.");
    }

    @Transactional
    public BaseResponseDTO<String> saveOrUpdateFcmToken(String userId, String fcmToken) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);

        userFcmTokenRepository.findByUser(user).ifPresentOrElse(
                existing -> {
                    existing.setFcmToken(fcmToken);
                    userFcmTokenRepository.save(existing);
                },
                () -> {
                    UserFcmTokenEntity newToken = UserFcmTokenEntity.builder()
                            .user(user)
                            .fcmToken(fcmToken)
                            .build();
                    userFcmTokenRepository.save(newToken);
                });

        return BaseResponseDTO.success("FCM 토큰 저장 / 갱신 완료", "fcm_token").addField("message", "FCM 토큰 저장 성공");
    }

    @Transactional
    public String deleteMemberShip(String accessToken, String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) throw new CustomException(ErrorCode.AUTH_005);

        refreshTokenRepository.deleteRefreshToken(userId);

        Date expirationDate = jwtTokenProvider.getExpiration(accessToken);
        long ttl = expirationDate.getTime() - System.currentTimeMillis();

        if (ttl > 0) {
            refreshTokenRepository.saveBlackList(accessToken, ttl);
            userFcmTokenService.deleteToken(userId);
        }

        user.getParticipants().clear();
        user.getRoles().clear();

        userRepository.deleteById(userId);
        return "success";
    }
}