package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserFindIdRequestDTO;
import com.github.hjgf0624.sideproject.dto.user.UserFindIdResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserPwdResetDTO;
import com.github.hjgf0624.sideproject.dto.user.UserPwdResetRequestDTO;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    public BaseResponseDTO<UserFindIdResponseDTO> findUserId(UserFindIdRequestDTO request) {
        UserEntity user = userRepository.findByNameAndPhoneNumber(request.getName(), request.getPhoneNum())
                .orElseThrow(() -> new RuntimeException("일치하는 사용자가 없습니다."));
        return BaseResponseDTO.success(new UserFindIdResponseDTO(user.getUserId()), "find_id");
    }

    public BaseResponseDTO<Void> sendResetCode(UserPwdResetRequestDTO request){
        UserEntity user = userRepository.findByUserId(request.getEmail());
        if (user == null){
            throw new RuntimeException("일치하는 사용자가 없습니다.");
        }
        sendEmailAuthCode(user.getUserId());
        return BaseResponseDTO.success(null, "인증 코드가 전송되었습니다.");
    }

//    public BaseResponseDTO<Void> verifyResetCode()

    public BaseResponseDTO<Void> resetPwd(UserPwdResetDTO request){
        UserEntity user = userRepository.findByUserId(request.getMail());
        if (user == null){
            throw new RuntimeException("일치하는 사용자가 없습니다.");
        }

        String hashedPwd = passwordEncoder.encode(request.getNewPwd());
        user.setUserPw(hashedPwd);
        userRepository.save(user);
        return BaseResponseDTO.success(null, "비밀번호가 성공적으로 변경되었습니다.");
    }

    private String sendEmailAuthCode(String email) {
        String code = generateCode(); // 랜덤 코드 생성

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + code);

        javaMailSender.send(message);
        return code; // 프론트엔드에서 이 코드를 클라이언트에 보관
    }

    private String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999)); // 6자리 난수
    }
}
