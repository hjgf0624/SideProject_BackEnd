package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.UserEntity;
//import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUserId(String username);

    @Query(value = """
            SELECT * FROM user
            WHERE ST_Distance_Sphere(Point(:longitude, :latitude), Point(longitude, latitude)) < 10000
            """, nativeQuery = true)
    List<UserEntity> findNearbyUsers(@Param("longitude") double longitude, @Param("latitude") double latitude);

    // 아이디 / 비밀번호 찾기용 메소드
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}
