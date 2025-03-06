package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.config.security.JwtTokenProvider;
import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import com.github.hjgf0624.sideproject.dto.user.*;
import com.github.hjgf0624.sideproject.entity.RoleEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.exception.CustomValidationException;
import com.github.hjgf0624.sideproject.repository.RefreshTokenRepository;
import com.github.hjgf0624.sideproject.repository.RoleRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
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

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String BIRTH_REGEX = "^(19|20)\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";


    public UserEntity toEntity(UserRegisterDTO dto, MultipartFile file) throws IOException {
        String profileImageUrl = file != null ?
                awsS3Service.uploadFile(file, dto.getEmail())
                : null;

        UserEntity userEntity = new UserEntity();

        userEntity.setUserId(dto.getEmail());
        userEntity.setUserPw(passwordEncoder.encode(dto.getPassword()));
        userEntity.setFirebaseToken(dto.getFcmToken());
        userEntity.setBirthdate(dto.getBirthDate());

        if (dto.getProfile() != null) {
            userEntity.setName(dto.getProfile().getName());
            userEntity.setNickname(dto.getProfile().getNickname());
            userEntity.setProfileImageUrl(profileImageUrl);
            userEntity.setPhoneNumber(dto.getProfile().getPhoneNumber());
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
        }

        return "success";
    }

    public BaseResponseDTO<UserLoginResponseDTO> refreshAccessToken(ReIssueTokenDTO reIssueTokenDTO)
            throws CustomValidationException {

        String accessToken = reIssueTokenDTO.getAccessToken();
        String refreshToken = reIssueTokenDTO.getRefreshToken();

        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);

        String userId = jwtTokenProvider.getUserPK(accessToken);

        System.out.println("userId: " + userId);

        if (jwtTokenProvider.isBlacklisted(accessToken)) {
            System.out.println("블랙리스트 들어옴");
            throw new CustomValidationException("Token is blacklisted");
        }

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            System.out.println("토큰 형식 맞는지 체크함 ");
            throw new CustomValidationException("Invalid refresh token");
        }

        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            System.out.println("만료 췤~");
            throw new CustomValidationException("Not expired access token");
        }

        if (!isValidRefreshToken(userId, refreshToken)) {
            throw new CustomValidationException("Token Expired");
        }

        UserEntity userEntity = userRepository.findByUserId(userId);

        List<String> authList = new ArrayList<>();
        for (RoleEntity role : userEntity.getRoles()) {
            authList.add(role.getRoleName());
        }

        String reIssueToken = jwtTokenProvider.createToken(userEntity.getUserId(), authList);

        UserLoginResponseDTO dto = UserLoginResponseDTO.builder()
                .email(userEntity.getUserId())
                .name(userEntity.getName())
                .build();

        return BaseResponseDTO.success(dto, "user")
                .addField("accessToken", reIssueToken)
                .addField("refreshToken", refreshToken)
                .addField("message", "Login successful");
    }

    public boolean isValidRefreshToken(String userId, String refreshToken) {
        String savedToken = refreshTokenRepository.getRefreshToken(userId);
        return savedToken != null && savedToken.equals(refreshToken);
    }

    public BaseResponseDTO<UserLoginResponseDTO> login(UserLoginDTO dto) {
        UserEntity savedUserInfo = userRepository.findByUserId(dto.getEmail());

        if (savedUserInfo == null || passwordEncoder.matches(savedUserInfo.getUserPw(), passwordEncoder.encode(dto.getPassword()))) {
            throw new CustomValidationException("Invalid credentials");
        }

        List<String> sAuthList = new ArrayList<>();
        for (RoleEntity role : savedUserInfo.getRoles()) {
            sAuthList.add(role.getRoleName());
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(savedUserInfo.getUserId()), sAuthList);
        String refreshToken = jwtTokenProvider.createRefreshToken(savedUserInfo.getUserId());

        refreshTokenRepository.saveRefreshToken(savedUserInfo.getUserId(), refreshToken);

        System.out.println(refreshTokenRepository.getRefreshToken(savedUserInfo.getUserId()));

        UserLoginResponseDTO userResponseDto = UserLoginResponseDTO.builder()
                .email(savedUserInfo.getUserId())
                .name(savedUserInfo.getName())
                .build();

        return BaseResponseDTO.success(userResponseDto, "user")
                .addField("accessToken", accessToken)
                .addField("refreshToken", refreshToken)
                .addField("message", "Login successful");
    }

    @Transactional
    public BaseResponseDTO<UserRegisterResponseDTO> register(UserRegisterDTO dto, MultipartFile file)
            throws CustomValidationException, IOException {
        Map<String, String> errors = new HashMap<>();
        UserEntity isUser = userRepository.findByUserId(dto.getEmail());

        if (isUser != null) {
            errors.put("email", "Email already in use");
            throw new CustomValidationException(errors, "Validation fail");
        }

        if(!dto.getPassword().matches(dto.getConfirmPassword())) {
            errors.put("password", "비밀번호가 일치하지 않습니다.");
        }
        if(!Pattern.matches(EMAIL_REGEX, dto.getEmail())) {
            errors.put("email", "이메일 형식이 올바르지 않습니다.");
        }
        if(!Pattern.matches(BIRTH_REGEX, dto.getBirthDate())) {
            errors.put("birth_date", "생년월일 형식이 올바르지 않습니다.");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors, "Validation fail");
        }

        UserEntity userEntity = toEntity(dto, file);
        RoleEntity roleEntity = roleRepository.findByRoleName("USER");

        userEntity.addRole(roleEntity);
        UserEntity savedUser = userRepository.save(userEntity);

        UserRegisterResponseDTO userDataDto = UserRegisterResponseDTO.builder()
                .userId(savedUser.getUserId())
                .firebaseToken(dto.getFcmToken())
                .birthDate(savedUser.getBirthdate())
                .profile(UserProfileDTO.builder()
                        .name(savedUser.getName())
                        .nickname(savedUser.getNickname())
                        .profileImageUrl(savedUser.getProfileImageUrl())
                        .phoneNumber(savedUser.getPhoneNumber())
                        .build())
                .location(LocationDTO.builder()
                        .latitude(savedUser.getLatitude())
                        .longitude(savedUser.getLongitude())
                        .build())
                .createdAt(savedUser.getCreatedAt())
                .build();

        return BaseResponseDTO.success(userDataDto, "data").addField("message", "Registration successful");
    }

    @Transactional
    public BaseResponseDTO<String> updateLocation(UpdateLocationDTO dto) {
        UserEntity entity = userRepository.findByUserId(dto.getUserId());

        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());

        userRepository.save(entity);

        return BaseResponseDTO.success("위치 정보 저장 성공.", "message");
    }

    public BaseResponseDTO<UserProfileDTO> getProfileInfo(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        UserProfileDTO dto = UserProfileDTO.builder()
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .profileImageUrl(userEntity.getProfileImageUrl())
                .phoneNumber(userEntity.getPhoneNumber())
                .build();

        return BaseResponseDTO.success(dto, "user_profile").addField("message", "프로필 정보 불러오기 성공.");
    }

    @Transactional
    public BaseResponseDTO<UserProfileDTO> updateProfileInfo(UserProfileUpdateDTO dto, MultipartFile file) throws IOException {

        String profileImageUrl = file != null ? awsS3Service.uploadFile(file, dto.getUserId()) : null;
        UserEntity userEntity = userRepository.findByUserId(dto.getUserId());

        userEntity.setName(dto.getName());
        userEntity.setNickname(dto.getNickname());
        userEntity.setPhoneNumber(dto.getPhoneNumber());
        userEntity.setProfileImageUrl(profileImageUrl);

        UserEntity savedUser = userRepository.save(userEntity);

        UserProfileDTO updateDTO = UserProfileDTO.builder()
                .name(savedUser.getName())
                .nickname(savedUser.getNickname())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .phoneNumber(savedUser.getPhoneNumber())
                .build();

        return BaseResponseDTO.success(updateDTO, "user_profile").addField("message", "프로필 정보 업데이트 성공.");
    }

    public UserEntity findUserById(String email) {
        return userRepository.findByUserId(email);
    }

}
