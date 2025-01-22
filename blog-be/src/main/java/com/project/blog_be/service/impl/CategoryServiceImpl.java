package com.project.blog_be.service.impl;

import com.project.blog_be.domain.entity.CategoryEntity;
import com.project.blog_be.repository.CategoryRepository;
import com.project.blog_be.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        String categoryEntityName = categoryEntity.getName();
        if (categoryRepository.existsByNameIgnoreCase(categoryEntityName)) {
            throw new IllegalArgumentException("Category already exist with name " + categoryEntityName);
        }

        return categoryRepository.save(categoryEntity);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            if (!category.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated with it");
            }

            categoryRepository.deleteById(id);
        }
    }

    @Override
    public CategoryEntity getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }
}
