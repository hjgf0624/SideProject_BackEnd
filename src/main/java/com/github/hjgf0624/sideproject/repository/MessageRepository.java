package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    // 메세지 아이디, 약속 시간을 기준으로 검색하는 메소드
//    List<MessageEntity> findByMessageIdAnd

    int countByMessageId(int messageId);
}
