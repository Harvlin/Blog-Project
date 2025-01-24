package com.project.blog_be.service;

import com.project.blog_be.domain.dto.CreatePostRequest;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<PostEntity> getAllPost(UUID catId, UUID tagId);
    List<PostEntity> getDraftPosts(UserEntity user);
    PostEntity createPost(UserEntity userEntity, CreatePostRequest createPostRequest);
}
