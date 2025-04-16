package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.message.MessageBroadcastRequestDTO;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.repository.MessageRepository;
import com.github.hjgf0624.sideproject.repository.UserFcmTokenRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageBroadcastService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final FCMService fcmService;

    // 메시지 브로드캐스트 처리
    @Transactional
    public Map<String, Object> broadcastMessage(MessageBroadcastRequestDTO request) {
        Long messageId = request.getMessageId();
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();

        // 메시지 가져오기
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        // 10km 내 사용자 조회
        List<UserEntity> nearbyUsers = userRepository.findNearbyUsers(latitude, longitude);

        // 토큰을 이용해 메시지 전송
        for (UserEntity user : nearbyUsers) {
            userFcmTokenRepository.findByUser(user).ifPresent(userFcmToken -> {
                try {
                    fcmService.sendBroadcastMessage(
                            message.getTitle(),
                            message.getContent(),
                            userFcmToken.getFcmToken()
                    );
                } catch (Exception e) {
                    // 예외 처리 (FCM 전송 실패시 등, printStackTrace 대신 다른 Logger 사용해야 할 듯)
                    e.printStackTrace();
                }
            });
        }
        // 성공적인 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "메시지가 성공적으로 전송되었습니다.");
        return response;
    }
}