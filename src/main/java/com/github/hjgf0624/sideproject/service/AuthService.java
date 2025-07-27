package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.EmailAuthDTO;
import com.github.hjgf0624.sideproject.dto.PhoneAuthDTO;
import com.github.hjgf0624.sideproject.dto.user.UserFindIdDTO;
import com.github.hjgf0624.sideproject.dto.user.UserFindIdResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserPwdResetDTO;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuthService {

    private final UserRepository userRepository;
    private final SmsAuthService smsAuthService;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    public BaseResponseDTO<UserFindIdResponseDTO> findUserId(UserFindIdDTO request) {
        UserEntity user = userRepository.findByPhoneNumber(request.getPhoneNum())
                .orElseThrow(() -> new RuntimeException("일치하는 사용자가 없습니다."));

        UserFindIdResponseDTO responseDTO = UserFindIdResponseDTO.builder()
                .email(user.getUserId())
                .created_At_id(user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();

        return BaseResponseDTO.success(responseDTO, "find_id");
    }

    public BaseResponseDTO<Void> resetPwd(UserPwdResetDTO request){
        UserEntity user = userRepository.findByUserId(request.getEmail());
        if (user == null){
            throw new RuntimeException("일치하는 사용자가 없습니다.");
        }

        String hashedPwd = passwordEncoder.encode(request.getNewPwd());
        user.setUserPw(hashedPwd);
        userRepository.save(user);
        return BaseResponseDTO.success(null, "비밀번호가 성공적으로 변경되었습니다.");
    }

    public BaseResponseDTO<Void> sendAuthCodeToEmail(EmailAuthDTO request) {
        UserEntity user = userRepository.findByUserId(request.getEmail());
        if (user == null){
            throw new RuntimeException("일치하는 사용자가 없습니다.");
        }

        String title = "이메일 인증 번호";
        String authCode = generateCode(); // 랜덤 코드 생성

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject(title);
        message.setText("인증 코드: " + authCode);
        javaMailSender.send(message);

        redisTemplate.opsForValue().set("auth_code:" + request.getEmail(), authCode, 3, TimeUnit.MINUTES);

        return BaseResponseDTO.success(null, "인증번호가 전송되었습니다.");
    }

    public BaseResponseDTO<Void> sendAuthCodeToPhone(PhoneAuthDTO request) {
        UserEntity user = userRepository.findByUserId(request.getPhone());
        if (user == null){
            throw new RuntimeException("일치하는 사용자가 없습니다.");
        }

        String authCode = smsAuthService.sendSmsAuthCode(request.getPhone());

        redisTemplate.opsForValue().set("sms_auth:" + request.getPhone(), authCode, 3, TimeUnit.MINUTES);
        log.info("인증번호 [{}]가 Redis에 저장되었습니다. (전화번호: {})", authCode, request.getPhone());

        return BaseResponseDTO.success(null, "인증번호가 전송되었습니다.");
    }

    private String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999)); // 6자리 난수
    }
}
