package com.github.hjgf0624.sideproject.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    public FCMService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendBroadcastMessage(String title, String content, String token) {
        if (token.isEmpty()) {
            System.out.println("토큰을 사용할 수 없습니다.");
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(content).build())
                .addToken(token)
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendMulticast(message);
            System.out.println("✅ 성공적으로 " + response.getSuccessCount() + " 명의 사용자에게 메시지 전달 성공");
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ FCM 기반 메시지 전송 실패: " + e.getMessage());
        }
    }
}