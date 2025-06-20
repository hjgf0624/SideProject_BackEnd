package com.github.hjgf0624.sideproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FcmTokenRequestDTO {
    private String userId;
    private String fcmToken;
}
