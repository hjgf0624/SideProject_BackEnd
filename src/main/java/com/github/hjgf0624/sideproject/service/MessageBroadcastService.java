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
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
@RequiredArgsConstructor
public class MessageBroadcastService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final FCMService fcmService;

    final static Logger logger = LogManager.getLogger(MessageBroadcastService.class);

    // 메시지 브로드캐스트 처리
    @Transactional
    public Map<String, Object> broadcastMessage(MessageBroadcastRequestDTO request) {
        // 메시지 가져오기
        MessageEntity message = messageRepository.findById(request.getMessageId())
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        double longitude = message.getLongitude();
        double latitude = message.getLatitude();

        System.out.println("쿼리용 위도: " + latitude + ", 경도: " + longitude);

        // 10km 내 사용자 조회
        List<UserEntity> nearbyUsers = userRepository.findNearbyUsers(longitude, latitude);

        System.out.println(nearbyUsers);

        // FCM 토큰 수집
        List<String> fcmTokens = nearbyUsers.stream()
                .map(userFcmTokenRepository::findByUser)
                .filter(Optional::isPresent)
                .map(optional -> optional.get().getFcmToken())
                .toList();
        // 토큰을 이용해 메시지 전송
        for (UserEntity user : nearbyUsers) {
            userFcmTokenRepository.findByUser(user).ifPresent(userFcmToken -> {
                try {
                    fcmService.sendBroadcastMessage(
                            message.getTitle(),
                            message.getContent(),
                            fcmTokens
                    );
                } catch (Exception e) {
                    // 예외 처리 (FCM 전송 실패시 등, printStackTrace 대신 다른 Logger 사용해야 할 듯)
                    logger.error("예외 발생 : {}",e.getMessage(), e);
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