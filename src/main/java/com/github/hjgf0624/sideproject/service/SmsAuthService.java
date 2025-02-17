package com.github.hjgf0624.sideproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SmsAuthService {
    private final String accessKey;
    private final String secretKey;
    private final String serviceId;
    private final String senderPhone;

    public String sendSmsAuthCode(String phone) {
        String code = generateCode();

        try {
            String url = "https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", accessKey);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", secretKey);
            conn.setDoOutput(true);

            String jsonBody = "{ \"type\": \"SMS\", \"from\": \"" + senderPhone + "\", \"to\": [\"" + phone + "\"], \"content\": \"인증 코드: " + code + "\" }";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 202 ? code : "FAIL";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    private String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
    }
}
