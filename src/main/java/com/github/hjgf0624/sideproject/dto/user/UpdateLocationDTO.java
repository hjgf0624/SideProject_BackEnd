package com.github.hjgf0624.sideproject.dto.user;

import com.github.hjgf0624.sideproject.dto.LocationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 위치정보 DTO")
public class UpdateLocationDTO extends LocationDTO {

    @Schema(
            description = "사용자 아이디",
            example = "test@example.com"
    )
    private String userId;
}
