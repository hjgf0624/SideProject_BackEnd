package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.CategorySimpleDTO;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequestDTO;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailResponseDTO;
import com.github.hjgf0624.sideproject.dto.user.UserProfileDTO;
import com.github.hjgf0624.sideproject.entity.*;
import com.github.hjgf0624.sideproject.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.LocationDTO;
import com.github.hjgf0624.sideproject.dto.message.*;
import com.github.hjgf0624.sideproject.exception.CustomValidationException;
import com.github.hjgf0624.sideproject.repository.MessageParticipantRepository;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public BaseResponseDTO<List<MessageGetResponseDTO>> getMessages(String userId, Double longitude, Double latitude) {
        List<MessageEntity> nearMessages = messageRepository.findNearbyMessages(longitude, latitude);

        List<MessageGetResponseDTO> responseDTO = nearMessages.stream()
                .map(msg -> MessageGetResponseDTO.builder()
                        .id(msg.getMessageId())
                        .date(msg.getMeetingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .time(msg.getMeetingDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .title(msg.getTitle())
                        .contents(msg.getContent())
                        .category(msg.getCategory().stream()
                                .map(mc -> {
                                    CategoryEntity category = mc.getCategory();
                                    return new CategorySimpleDTO(category.getCategoryId(), category.getCategoryName());
                                })
                                .collect(Collectors.toList()))
                        .memberList(msg.getParticipants().stream()
                                .map(participant -> new MessageParticipantDTO(
                                        participant.getParticipantType(),
                                        new UserProfileDTO(
                                                participant.getUser().getUserId(),
                                                participant.getUser().getNickname(),
                                                participant.getUser().getProfileImageUrl(),
                                                participant.getUser().getPhoneNumber(),
                                                participant.getUser().getBirthdate(),
                                                participant.getUser().getSex(),
                                                new LocationDTO(participant.getUser().getLatitude(), participant.getUser().getLongitude())
                                        )))
                        .collect(Collectors.toList()))
                        .location(LocationDTO.builder().longitude(msg.getLongitude()).latitude(msg.getLatitude()).build())
                        .recruitCount(MessageGetResponseDTO.RecruitCount.builder()
                                .current(messageParticipantRepository.countByMessage_MessageId(msg.getMessageId()))
                                .max(msg.getRecruitCount())
                                .build())
                        .build())
                .toList();

        return BaseResponseDTO.success(responseDTO, "messages");
        
    }

    public MsgDetailResponseDTO getMessageDetail(MsgDetailRequestDTO request) {
        Optional<MessageEntity> messageOpt = messageRepository.findById(request.getMessageId());

        if (messageOpt.isEmpty()) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        MessageEntity message = messageOpt.get();

        return MsgDetailResponseDTO.builder()
                .success(true)
                .title(message.getTitle())
                .content(message.getContent())
                .meetingDateTime(message.getMeetingDateTime())
                .createdAt(message.getCreatedAt())
                .capacityMemberNum(message.getRecruitCount())
                .currentMemberNum(messageParticipantRepository.countByMessage_MessageId(message.getMessageId()))
                .category(message.getCategory().stream()
                        .map(mc -> new CategorySimpleDTO(
                                mc.getCategory().getCategoryId(),
                                mc.getCategory().getCategoryName()
                        ))
                        .collect(Collectors.toList()))
                .memberList(message.getParticipants().stream()
                        .map(participant -> new MessageParticipantDTO(
                                participant.getParticipantType(),
                                new UserProfileDTO(
                                    participant.getUser().getUserId(),
                                    participant.getUser().getNickname(),
                                    participant.getUser().getProfileImageUrl(),
                                    participant.getUser().getPhoneNumber(),
                                    participant.getUser().getBirthdate(),
                                    participant.getUser().getSex(),
                                    new LocationDTO(participant.getUser().getLatitude(), participant.getUser().getLongitude())
                                )))
                        .collect(Collectors.toList()))
                .build();
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
                .participantType(ParticipantTypeEntity.PUBLISHER)
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

        return BaseResponseDTO.success(responseDTO, "data")
                .addField("message", "메시지 생성 성공")
                .addField("isExist", false);
    }

    public BaseResponseDTO<List<MessageGetResponseDTO>> getMessagesByDate(MessageGetRequestDTO dto) throws CustomValidationException {
        List<MessageEntity> messages = messageRepository.findUserMessagesByDate(dto.getUserId(), dto.getDate());

        if (messages.isEmpty()) {
            return null;
        }

        List<MessageGetResponseDTO> responseDTO = messages.stream()
                .map(msg -> MessageGetResponseDTO.builder()
                        .id(msg.getMessageId())
                        .date(msg.getMeetingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .time(msg.getMeetingDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .title(msg.getTitle())
                        .contents(msg.getContent())
                        .location(LocationDTO.builder().longitude(msg.getLongitude()).latitude(msg.getLatitude()).build())
                        .category(msg.getCategory().stream()
                                .map(mc -> new CategorySimpleDTO(
                                        mc.getCategory().getCategoryId(),
                                        mc.getCategory().getCategoryName()
                                ))
                                .collect(Collectors.toList()))
                        .recruitCount(MessageGetResponseDTO.RecruitCount.builder()
                                .current(messageParticipantRepository.countByMessage_MessageId(msg.getMessageId()))
                                .max(msg.getRecruitCount())
                                .build())
                        .build())
                .toList();

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
                .participantType(ParticipantTypeEntity.PARTICIPANT)
                .build();

        messageParticipantRepository.save(participant);

        return ResponseEntity.ok(Map.of("success", true));
    }

    public BaseResponseDTO<List<String>> getMessageDate(String userId) throws CustomValidationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<MessageParticipantEntity> participantEntities = messageParticipantRepository.findAllByUser_UserId(userId);
        List<String> messageDates = participantEntities.stream()
                .map(participant -> participant.getMessage().getMessageId())
                .map(id -> messageRepository.findById(id).map(MessageEntity::getMeetingDateTime))
                .flatMap(Optional::stream)
                .map(dateTime -> dateTime.format(formatter)).distinct()
                .toList();

        return BaseResponseDTO.success(messageDates, "messageDates");
    }
}

