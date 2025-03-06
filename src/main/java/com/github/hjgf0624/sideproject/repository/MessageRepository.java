package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query("SELECT m FROM MessageEntity m WHERE ST_Distance("
            + "ST_GeomFromText(CONCAT('POINT(', :longitude, ' ', :latitude, ')'), 4326), "
            + "ST_GeomFromText(CONCAT('POINT(', m.longitude, ' ', m.latitude, ')'), 4326)) * 111000 < 10000")
    List<MessageEntity> findNearbyMessages(@Param("latitude") double latitude, @Param("longitude") double longitude);
  
    int countByMessageId(int messageId);
}

