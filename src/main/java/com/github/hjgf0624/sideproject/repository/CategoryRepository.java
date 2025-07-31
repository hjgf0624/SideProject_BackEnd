package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.CategoryEntity;
import com.github.hjgf0624.sideproject.entity.MessageCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByCategoryName(String name);
}
