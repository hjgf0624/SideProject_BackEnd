package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.entity.UserFcmTokenEntity;
import com.github.hjgf0624.sideproject.exception.CustomException;
import com.github.hjgf0624.sideproject.exception.ErrorCode;
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
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_005));

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
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_005));

        userFcmTokenRepository.findByUser(user).ifPresentOrElse(
                existing -> userFcmTokenRepository.deleteById(existing.getId()),
                () -> {
                    throw new CustomException(ErrorCode.FCM_NO_TOKENS);
                }
        );
    }
}
