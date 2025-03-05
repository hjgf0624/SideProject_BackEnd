package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.LocationDto;
import com.github.hjgf0624.sideproject.dto.alarm.AlarmResponse;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final MessageRepository messageRepository;

    // 특정 사용자의 알람 리스트 조회 (위치 기반)
    public List<AlarmResponse> getAlarmList(String userId, LocationDto location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // 가까운 메시지를 찾기 위해 적절한 거리 기준을 설정
        List<MessageEntity> messages = messageRepository.findNearbyMessages(latitude, longitude);

        // 메시지 데이터를 알람 형태로 변환
        return messages.stream()
                .map(AlarmResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
