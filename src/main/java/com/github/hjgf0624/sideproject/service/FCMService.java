package com.github.hjgf0624.sideproject.service;

import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    @Autowired
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
            System.out.println("✅ Successfully sent message to " + response.getSuccessCount() + " users.");
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ Error sending FCM message: " + e.getMessage());
        }
    }
}