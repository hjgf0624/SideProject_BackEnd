package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.MessageParticipantEntity;
import com.github.hjgf0624.sideproject.entity.MessageParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageParticipantRepository extends JpaRepository<MessageParticipantEntity, MessageParticipantId> {
    int countByMessage_MessageId(Long messageId);
    List<MessageParticipantEntity> findAllByUser_UserId(String userId);
}
