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

        System.out.println(user.toString());

        userFcmTokenRepository.findByUser(user).ifPresentOrElse(
                existing -> existing.setFcmToken(fcmToken),
                () -> userFcmTokenRepository.save(UserFcmTokenEntity.builder()
                        .user(user)
                        .fcmToken(fcmToken)
                        .build())
        );
    }
}
