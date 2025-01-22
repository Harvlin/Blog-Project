package com.project.blog_be.repository;

import com.project.blog_be.domain.PostStatus;
import com.project.blog_be.domain.entity.CategoryEntity;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.TagEntity;
import com.project.blog_be.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findAllByStatusAndCategoryAndTagsContaining(PostStatus postStatus, CategoryEntity categoryEntity, TagEntity tagEntity);
    List<PostEntity> findAllByStatusAndCategory(PostStatus postStatus, CategoryEntity categoryEntity);
    List<PostEntity> findAllByStatusAndTagsContaining(PostStatus postStatus, TagEntity tagEntity);
    List<PostEntity> findAllByStatus(PostStatus postStatus);
    List<PostEntity> findAllByAuthorAndStatus(UserEntity author, PostStatus postStatus);
}
