package com.github.hjgf0624.sideproject.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Slf4j
@Service
@RequiredArgsConstructor
public class SmsAuthService {

    private static final SecureRandom secureRandom = new SecureRandom();

    private DefaultMessageService messageService;

    @Value("${sms.sender-phone}")
    private String senderPhone;

    @Value("${sms.access-key}")
    private String apiKey;

    @Value("${sms.secret-key}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 인증번호 생성 (6자리 난수)
     */
    private String generateCode() {
        int randomNumber = 100000 + secureRandom.nextInt(900000);
        log.info("Generated SMS Code: {}", randomNumber);
        return String.valueOf(randomNumber);
    }

    /**
     * SMS 인증번호 전송
     */
    public String sendSmsAuthCode(String phone) {
        String code = generateCode();

        try {
            Message message = new Message();
            message.setFrom(senderPhone);
            message.setTo(phone);
            message.setText("[SMS] 인증번호: " + code + "를 입력하세요.");

            log.info("Sending SMS to {} with content: {}", phone, message.getText());

            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            log.info("SMS Send Response: {}", response);

            return code; // 프론트에서 인증번호 관리
        } catch (Exception e) {
            log.error("SMS 전송 중 오류 발생", e);
            return "ERROR";
        }
    }
}