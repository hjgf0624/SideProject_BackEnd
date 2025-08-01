package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.CategorySimpleDTO;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import com.github.hjgf0624.sideproject.dto.alarm.AlarmResponseDTO;
import com.github.hjgf0624.sideproject.entity.MessageCategoryEntity;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.entity.ParticipantTypeEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.repository.MessageRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    // 특정 사용자의 알람 리스트 조회 (위치 기반)
    public List<AlarmResponseDTO> getAlarmList(String userId) {
        UserEntity user = userRepository.findByUserId(userId);

        double longitude = user.getLongitude();
        double latitude = user.getLatitude();

        // 게시글 반경 10km 이내의 유저를 구하는 로직!
//        double minLat = savedMessage.getLatitude() - 0.09;
//        double maxLat = savedMessage.getLatitude() + 0.09;
//        double minLng = savedMessage.getLongitude() - 0.09;
//        double maxLng = savedMessage.getLongitude() + 0.09;
//
//        List<UserEntity> nearUsers = userRepository.findUsersWithinLatLngRange(minLat, maxLat, minLng, maxLng);

        // 가까운 메시지를 찾기 위해 적절한 거리 기준을 설정
        List<MessageEntity> messages = messageRepository.findNearbyMessages(longitude, latitude);

        // 메시지 데이터를 알람 형태로 변환
        return messages.stream()
                .map(msg -> new AlarmResponseDTO(
                        msg.getMessageId(),
                        msg.getTitle(),
                        msg.getCategory().stream()
                                .map(mc -> new CategorySimpleDTO(mc.getCategory().getCategoryId(), mc.getCategory().getCategoryName()))
                                .collect(Collectors.toList()),
                        msg.getCreatedAt(),
                        msg.getParticipants().stream()
                                .filter(p -> p.getParticipantType() == ParticipantTypeEntity.PUBLISHER)
                                .map(p -> p.getUser().getProfileImageUrl())
                                .findFirst()
                                .orElse(null)
                ))
                .collect(Collectors.toList());
    }
}
