package com.project.blog_be.service;

import com.project.blog_be.domain.dto.request.CreatePostRequest;
import com.project.blog_be.domain.dto.request.UpdatePostRequest;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface PostService {

    PostEntity getPost(UUID id);
    List<PostEntity> getAllPost(UUID catId, UUID tagId);
    List<PostEntity> getDraftPosts(UserEntity user);
    PostEntity createPost(UserEntity user, CreatePostRequest createPostRequest);
    PostEntity updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);
}
