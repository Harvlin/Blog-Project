package com.project.blog_be.repository;

import com.project.blog_be.domain.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, UUID> {

    @Query("SELECT t FROM TagEntity t LEFT JOIN FETCH t.posts")
    List<TagEntity> findAllWithPostCount();

    List<TagEntity> findByNameInIgnoreCase(Set<String> names);
}
