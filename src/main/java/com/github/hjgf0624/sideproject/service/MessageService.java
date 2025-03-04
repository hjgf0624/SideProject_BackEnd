package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import com.github.hjgf0624.sideproject.dto.message.*;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.entity.MessageParticipantEntity;
import com.github.hjgf0624.sideproject.entity.MessageParticipantId;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.exception.CustomValidationException;
import com.github.hjgf0624.sideproject.repository.MessageParticipantRepository;
import com.github.hjgf0624.sideproject.repository.MessageRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import javax.xml.bind.ValidationException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageParticipantRepository messageParticipantRepository;

    public MessageEntity toEntity(MessageRequestDTO messageRequestDTO) {
        MessageEntity messageEntity = new MessageEntity();

        // 메세지의 카테고리가 현재 null, 회의 진행해야 할듯 함
        messageEntity.setUserId(messageRequestDTO.getUserId());
        messageEntity.setTitle(messageRequestDTO.getTitle());
        messageEntity.setContent(messageRequestDTO.getContent());

        messageEntity.setMeetingDateTime(messageRequestDTO.getMeetingDateTime());
        messageEntity.setAnonymous(messageRequestDTO.isAnonymous());
        messageEntity.setRecruitCount(messageRequestDTO.getRecruitCount());

        LocationDTO locationDTO = messageRequestDTO.getLocation();
        messageEntity.setLatitude(locationDTO.getLatitude());
        messageEntity.setLongitude(locationDTO.getLongitude());

        return messageEntity;
    }

    @Transactional
    public BaseResponseDTO<MessageResponseDTO> saveMessage(MessageRequestDTO dto) {
        MessageEntity message = toEntity(dto);
        MessageEntity savedMessage = messageRepository.save(message);

        MessageParticipantId participantId = new MessageParticipantId(savedMessage.getUserId(), savedMessage.getMessageId());
        MessageParticipantEntity participant = MessageParticipantEntity.builder()
                .id(participantId)
                .user(userRepository.findByUserId(dto.getUserId()))
                .message(message)
                .build();

        messageParticipantRepository.save(participant);

        MessageResponseDTO responseDTO = MessageResponseDTO.builder()
                .messageId(savedMessage.getMessageId())
                .userId(savedMessage.getUserId())
                .title(savedMessage.getTitle())
                .content(savedMessage.getContent())
                .anonymous(savedMessage.isAnonymous())
                .meetingDateTime(savedMessage.getMeetingDateTime())
                .recruitCount(savedMessage.getRecruitCount())
//                .categories()
                .location(LocationDTO.builder()
                        .latitude(savedMessage.getLatitude())
                        .longitude(savedMessage.getLongitude())
                        .build())
                .createdAt(savedMessage.getCreatedAt())
                .build();

        // 게시글 반경 10km 이내의 유저를 구하는 로직!
//        double minLat = savedMessage.getLatitude() - 0.09;
//        double maxLat = savedMessage.getLatitude() + 0.09;
//        double minLng = savedMessage.getLongitude() - 0.09;
//        double maxLng = savedMessage.getLongitude() + 0.09;
//
//        List<UserEntity> nearUsers = userRepository.findUsersWithinLatLngRange(minLat, maxLat, minLng, maxLng);

        return BaseResponseDTO.success(responseDTO, "data")
                .addField("message", "Message created successfully")
                .addField("isExist", false);
    }

    public BaseResponseDTO<MessageGetResponseDTO> getMessage(MessageGetRequestDTO dto) throws CustomValidationException {
        MessageEntity message = messageRepository.findById(dto.getMessageId()).orElse(null);

        if (message == null) {
            throw new CustomValidationException("메세지 ID가 없습니다.");
        }

        String date = message.getMeetingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = message.getMeetingDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        int currentCount = messageRepository.countByMessageId(message.getMessageId());

        MessageGetResponseDTO responseDTO = MessageGetResponseDTO.builder()
                .date(date)
                .time(time)
                .title(message.getTitle())
                .contents(message.getContent())
                .recruitCount(MessageGetResponseDTO.RecruitCount.builder()
                        .current(currentCount)
                        .max(message.getRecruitCount())
                        .build())
                .build();

        return BaseResponseDTO.success(responseDTO, "message");
    }

    public ResponseEntity<Map<String, Boolean>> joinMessage(JoinMessageDTO dto) throws CustomValidationException {
        UserEntity user = userRepository.findById(dto.getUserId()).orElse(null);
        MessageEntity message = messageRepository.findById(dto.getMessageId()).orElse(null);

        if (user == null || message == null) {
            throw new CustomValidationException("존재하지 않는 유저 혹은 메세지 입니다.");
        }

        MessageParticipantId participantId = new MessageParticipantId(user.getUserId(), message.getMessageId());
        Optional<MessageParticipantEntity> optionalMessageParticipant = messageParticipantRepository.findById(participantId);
        if (optionalMessageParticipant.isPresent()) {
            throw new CustomValidationException("이미 참여한 메세지 입니다.");
        }

        MessageParticipantEntity participant = MessageParticipantEntity.builder()
                .id(participantId)
                .user(user)
                .message(message)
                .build();

        messageParticipantRepository.save(participant);

        return ResponseEntity.ok(Map.of("success", true));
    }

}
