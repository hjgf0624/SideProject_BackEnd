package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.CategorySimpleDTO;
import com.github.hjgf0624.sideproject.entity.CategoryEntity;
import com.github.hjgf0624.sideproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public BaseResponseDTO<List<CategorySimpleDTO>> getCategories() {
        List<CategorySimpleDTO> categories = categoryRepository.findAll().stream()
                .map(category -> new CategorySimpleDTO(category.getCategoryId(), category.getCategoryName()))
                .toList();

        return BaseResponseDTO.success(categories, "category");
    }

    public BaseResponseDTO<CategorySimpleDTO> getCategoryByCategoryName(String categoryName) {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryName);

        return BaseResponseDTO.success(new CategorySimpleDTO(categoryEntity.getCategoryId(), categoryEntity.getCategoryName()), "category");
    }
}
