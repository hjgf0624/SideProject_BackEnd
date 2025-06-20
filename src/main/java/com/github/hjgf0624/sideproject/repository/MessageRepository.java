package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query(value = """
        SELECT * FROM job_message
        WHERE ST_Distance_Sphere(Point(:longitude, :latitude),Point(longitude, latitude)) < 10000""", nativeQuery = true)
    List<MessageEntity> findNearbyMessages(@Param("longitude") double longitude, @Param("latitude") double latitude);


    @Query("""
        SELECT m FROM MessageEntity m
        WHERE m.messageId IN (
            SELECT mp.id.messageId FROM MessageParticipantEntity mp WHERE mp.id.userId = :userId
        ) AND FUNCTION('DATE', m.meetingDateTime) = :date
    """)
    List<MessageEntity> findUserMessagesByDate(@Param("userId") String userId, @Param("date") LocalDate date);

    int countByMessageId(Long messageId);
}

