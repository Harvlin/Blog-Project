package com.project.blog_be.service;

import com.project.blog_be.domain.entity.CategoryEntity;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryEntity> listCategories();

    CategoryEntity createCategory(CategoryEntity categoryEntity);

    void deleteCategory(UUID id);
}
