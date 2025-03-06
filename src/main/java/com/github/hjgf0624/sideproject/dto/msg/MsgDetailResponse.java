package com.github.hjgf0624.sideproject.dto.msg;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MsgDetailResponse {
    private boolean success;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int capacityMemberNum;  // 모집하는 총 인원 수
    private int currentMemberNum;   // 현재 참여한 인원 수
    private List<String> memberList;  // 멤버 리스트
}
