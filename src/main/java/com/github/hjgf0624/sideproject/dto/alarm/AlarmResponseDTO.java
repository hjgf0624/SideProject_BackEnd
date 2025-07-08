package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Schema(description = "알람 Response DTO")
public class AlarmResponseDTO {
    @Schema(description = "메시지 ID")
    private Long messageId;

    @Schema(description = "메시지 제목")
    private String title;

    @Schema(description = "카테고리")
    private Integer category;

    @Schema(description = "생성날짜")
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
