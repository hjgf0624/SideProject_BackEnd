package com.github.hjgf0624.sideproject.config.security.oauth;

import com.github.hjgf0624.sideproject.config.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // CustomOAuth2User에서 email 꺼냄
        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
        String email = customUser.getEmail();

        // Access, Refresh Token 발급
        String accessToken = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        // Redis에 refreshToken 저장
         redisTemplate.opsForValue().set("refresh:" + email, refreshToken, 24, TimeUnit.HOURS);

        // 프론트로 리다이렉트 (token 전달)
        String redirectUri = "https://your-frontend.com/oauth2/success?accessToken=" + accessToken;
        response.sendRedirect(redirectUri);
    }
}
