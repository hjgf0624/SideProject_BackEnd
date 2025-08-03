package com.github.hjgf0624.sideproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private List<String> fcmToken;
}