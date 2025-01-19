package com.project.blog_be.repository;

import com.project.blog_be.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.posts")
    List<CategoryEntity> findAllWithPostCount();

    boolean existsByNameIgnoreCase(String name);
}
