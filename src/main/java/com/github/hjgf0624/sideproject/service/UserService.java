package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserLoginDTO;
import com.github.hjgf0624.sideproject.dto.user.UserLoginResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserRegisterDTO;
import com.github.hjgf0624.sideproject.dto.user.UserRegisterResponseDTO;
import com.github.hjgf0624.sideproject.entity.RoleEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.exception.CustomValidationException;
import com.github.hjgf0624.sideproject.repository.RoleRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AwsS3Service awsS3Service;
    private final PasswordEncoder passwordEncoder;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String BIRTH_REGEX = "^(19|20)\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public BaseResponseDTO<UserLoginResponseDTO> login(UserLoginDTO dto, String token) {
        UserEntity savedUserInfo = userRepository.findByUserId(dto.getEmail());

        if (savedUserInfo == null || passwordEncoder.matches(savedUserInfo.getUserPw(), passwordEncoder.encode(dto.getPassword()))) {
            throw new CustomValidationException(null, "Invalid credentials");
        }

        UserLoginResponseDTO userResponseDto = UserLoginResponseDTO.builder()
                .email(savedUserInfo.getUserId())
                .name(savedUserInfo.getName())
                .build();

        return BaseResponseDTO.success(userResponseDto, "user")
                .addField("token", token)
                .addField("message", "Login successful");
    }

    public UserEntity toEntity(UserRegisterDTO dto, MultipartFile file) throws IOException {
        String profileImageUrl = file != null ?
                awsS3Service.uploadFile(file, dto.getEmail())
                : null;

        System.out.println(profileImageUrl);
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

    @Transactional
    public BaseResponseDTO<UserRegisterResponseDTO> register(UserRegisterDTO dto, MultipartFile file)
            throws CustomValidationException, IOException {
        Map<String, String> errors = new HashMap<>();

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
                .profile(UserRegisterResponseDTO.ProfileDTO.builder()
                        .name(savedUser.getName())
                        .nickname(savedUser.getNickname())
                        .profileImageUrl(savedUser.getProfileImageUrl())
                        .phoneNumber(savedUser.getPhoneNumber())
                        .build())
                .location(UserRegisterResponseDTO.LocationDTO.builder()
                        .latitude(savedUser.getLatitude())
                        .longitude(savedUser.getLongitude())
                        .build())
                .createdAt(savedUser.getCreatedAt())
                .build();

        return BaseResponseDTO.success(userDataDto, "data").addField("message", "dasfasfd");
    }

    public UserEntity findUserById(String email) {
        return userRepository.findByUserId(email);
    }

}
