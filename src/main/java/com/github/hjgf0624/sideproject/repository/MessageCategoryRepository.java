package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.MessageCategoryEntity;
import com.github.hjgf0624.sideproject.entity.MessageCategoryId;
import com.github.hjgf0624.sideproject.entity.MessageParticipantEntity;
import com.github.hjgf0624.sideproject.entity.MessageParticipantId;
import jakarta.mail.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageCategoryRepository extends JpaRepository<MessageCategoryEntity, MessageCategoryId> {
}
