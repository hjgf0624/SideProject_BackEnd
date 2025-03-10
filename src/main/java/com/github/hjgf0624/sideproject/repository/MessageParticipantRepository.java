package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.MessageParticipantEntity;
import com.github.hjgf0624.sideproject.entity.MessageParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageParticipantRepository extends JpaRepository<MessageParticipantEntity, MessageParticipantId> {
    int countByMessage_MessageId(Long messageId);
}
