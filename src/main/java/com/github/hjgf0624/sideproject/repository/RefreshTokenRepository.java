package com.github.hjgf0624.sideproject.repository;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private final StringRedisTemplate stringRedisTemplate;
    private static final long REFRESH_TOKEN_EXPIRATION = 1;

    public RefreshTokenRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        stringRedisTemplate.opsForValue().set("refresh:" + userId, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.DAYS);
    }

    public void saveBlackList(String accessToken, long ttl) {
        stringRedisTemplate.opsForValue().set("blacklist:" + accessToken, "expired", ttl, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return stringRedisTemplate.opsForValue().get("refresh:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        stringRedisTemplate.delete("refresh:" + userId);
    }

}
