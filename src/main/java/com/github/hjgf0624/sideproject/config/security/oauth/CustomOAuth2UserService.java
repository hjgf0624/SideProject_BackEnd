package com.github.hjgf0624.sideproject.config.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String name = null;

        switch (registrationId){
            case "google" -> {
                email = oAuth2User.getAttribute("email");
                name = oAuth2User.getAttribute("name");
            }

            case "kakao" -> {
                Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
                if (kakaoAccount != null) {
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    email = (String) kakaoAccount.get("email");
                    name = profile != null ? (String) profile.get("nickname") : null;
                }
            }

            case "naver" -> {
                Map<String, Object> response = oAuth2User.getAttribute("response");
                if (response != null) {
                    email = (String) response.get("email");
                    name = (String) response.get("name");
                }

            }

            default -> throw new OAuth2AuthenticationException("확인되지 않은 Auth ID 입니다 : " + registrationId);
        }

        return oAuth2User;
    }
}
