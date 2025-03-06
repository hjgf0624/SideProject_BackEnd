package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUserId(String username);

    @Query(value = """
        SELECT * FROM user
        WHERE latitude BETWEEN ?1 AND ?2
        AND longitude BETWEEN ?3 AND ?4
    """, nativeQuery = true)
    List<UserEntity> findUsersWithinLatLngRange(
            Double minLatitude,
            Double maxLatitude,
            Double minLongitude,
            Double maxLongitude);
}
