package com.github.hjgf0624.sideproject.dto.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MsgDetailRequestDTO {
    private Long messageId;  // 메시지 고유 ID
}
