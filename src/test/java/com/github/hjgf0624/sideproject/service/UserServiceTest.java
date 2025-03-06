package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.message.JoinMessageDTO;
import com.github.hjgf0624.sideproject.dto.user.ReIssueTokenDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void logoutTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwicm9sZXMiOltdLCJpYXQiOjE3NDEwODcyNjEsImV4cCI6MTc0MTA4NzMyMX0.zv5bfJEhjk8PAnXzkpRvTTJkJ_xQTRBFSmUOYL9KMtw";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NDExNzM2NjF9.KwwR02Wg6lASSeag5hT5LviMiyaif1LiP9zmDc1Kv5E";

        ReIssueTokenDTO reIssueTokenDTO = new ReIssueTokenDTO();
        reIssueTokenDTO.setAccessToken(accessToken);
        reIssueTokenDTO.setRefreshToken(refreshToken);

        userService.refreshAccessToken(reIssueTokenDTO);
    }
}
