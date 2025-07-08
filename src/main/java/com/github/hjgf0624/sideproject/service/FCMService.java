package com.github.hjgf0624.sideproject.service;

import com.google.firebase.messaging.*;
import kotlinx.serialization.Required;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final Logger log = LoggerFactory.getLogger(FCMService.class);

    public void sendBroadcastMessage(String title, String content, List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            System.out.println("전송할 토큰이 없습니다.");
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(content)
                        .build())
                .addAllTokens(tokens)
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            int successCnt = (int) response.getResponses().stream().filter(SendResponse::isSuccessful).count();

            log.info("FCM 메시지 전송 성공: {}명", successCnt);

            response.getResponses().forEach(resp -> {
                if(!resp.isSuccessful()) {
                    log.error("FCM 메시지 전송 실패 : {}", resp.getException().getMessage());
                }
            });

        } catch (FirebaseMessagingException e) {
            log.error("전체 FCM 전송 중 예외 발생", e);
        }
    }
}