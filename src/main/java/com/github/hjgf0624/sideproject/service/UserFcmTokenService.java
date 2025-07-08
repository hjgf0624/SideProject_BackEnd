package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.entity.UserFcmTokenEntity;
import com.github.hjgf0624.sideproject.repository.UserFcmTokenRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserFcmTokenService {

    private final UserRepository userRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional
    public void saveOrUpdateToken(String userId, String fcmToken) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        userFcmTokenRepository.findByUser(user).ifPresentOrElse(
                existing -> existing.setFcmToken(fcmToken),
                () -> userFcmTokenRepository.save(UserFcmTokenEntity.builder()
                        .user(user)
                        .fcmToken(fcmToken)
                        .build())
        );
    }

    @Transactional
    public void deleteToken(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        userFcmTokenRepository.findByUser(user).ifPresentOrElse(
                existing -> userFcmTokenRepository.deleteById(existing.getId()),
                () -> {
                    throw new RuntimeException("해당 유저의 FCM 토큰이 존재하지 않습니다.");
                }
        );
    }
}
