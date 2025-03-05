package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequest;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailResponse;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public MsgDetailResponse getMessageDetail(MsgDetailRequest request) {
        Optional<MessageEntity> messageOpt = messageRepository.findById(request.getMessageId());

        if (messageOpt.isEmpty()) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        MessageEntity message = messageOpt.get();

        return MsgDetailResponse.builder()
                .success(true)
                .title(message.getTitle())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .capacityMemberNum(message.getCapacityMemberNum())
                .currentMemberNum(message.getCurrentMemberNum())
                //.memberList(message.getMemberList())
                .build();
    }
}