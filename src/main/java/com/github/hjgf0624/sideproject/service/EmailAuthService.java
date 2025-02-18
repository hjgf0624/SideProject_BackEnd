package com.github.hjgf0624.sideproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailAuthService {
    private final JavaMailSender mailSender;

    public String sendEmailAuthCode(String email) {
        String code = generateCode(); // 랜덤 코드 생성

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + code);

        mailSender.send(message);
        return code; // 프론트엔드에서 이 코드를 클라이언트에 보관
    }

    private String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999)); // 6자리 난수
    }
}
