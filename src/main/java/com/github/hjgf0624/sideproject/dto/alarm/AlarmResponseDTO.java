package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class AlarmResponseDTO {
    private Long messageId;
    private String title;
    private Integer category;
    private String createdAt;
//    private String profileImg;

    public static AlarmResponseDTO fromEntity(MessageEntity entity) {
        return new AlarmResponseDTO(
                entity.getMessageId(), //메시지 ID 조회
                entity.getTitle(), // 제목 조회
                entity.getCategoryId(), //카테고리 조회
                entity.getCreatedAt().toString()//생성날짜 조회
//                entity.getProfileImg() // 사진 조회
        );
    }
}
