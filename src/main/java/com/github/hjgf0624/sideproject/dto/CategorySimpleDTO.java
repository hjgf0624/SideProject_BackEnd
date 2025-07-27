package com.github.hjgf0624.sideproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "카테고리 DTO")
public class CategorySimpleDTO {
    private Long categoryId;
    private String categoryName;

}
