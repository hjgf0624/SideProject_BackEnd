package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.entity.UserFcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFcmTokenRepository extends JpaRepository<UserFcmTokenEntity, Long> {
    Optional<UserFcmTokenEntity> findByUser(UserEntity user);
}