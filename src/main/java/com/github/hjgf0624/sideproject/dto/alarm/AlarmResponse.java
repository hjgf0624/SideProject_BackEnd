package com.github.hjgf0624.sideproject.dto.alarm;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Long messageId;
    private String title;
    //private Set<String> category;
    //private String createdAt;
    //private String profileUrl;

    public static AlarmResponse fromEntity(MessageEntity entity) {
        return new AlarmResponse(
                entity.getMessageId(),
                entity.getTitle()
                //entity.getCategories(), //카테고리조회
                //entity.getCreatedAt().toString() //생성날짜조회
                //entity.getProfileUrl() // 사진조회
        );
    }
}
