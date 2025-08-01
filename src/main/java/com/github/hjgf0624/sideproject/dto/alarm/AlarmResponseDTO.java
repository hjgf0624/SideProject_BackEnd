package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.dto.CategorySimpleDTO;
import com.github.hjgf0624.sideproject.entity.CategoryEntity;
import com.github.hjgf0624.sideproject.entity.MessageCategoryEntity;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<CategorySimpleDTO> category;

    @Schema(description = "생성날짜")
    private LocalDateTime createdAt;

    @Schema(description = "프로필 이미지 URL")
    private String profileImg;

//    public static AlarmResponseDTO fromEntity(MessageEntity entity) {
//        return new AlarmResponseDTO(
//                entity.getMessageId(), //메시지 ID 조회
//                entity.getTitle(), // 제목 조회
//                entity.getCategory(),// 카테고리 조회
//                entity.getCreatedAt(),//생성날짜 조회
//                entity.get() // 사진 조회
//        );
//    }
}
