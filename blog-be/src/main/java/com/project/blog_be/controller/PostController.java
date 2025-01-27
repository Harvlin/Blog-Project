package com.project.blog_be.controller;

import com.project.blog_be.domain.dto.request.CreatePostRequest;
import com.project.blog_be.domain.dto.PostDto;
import com.project.blog_be.domain.dto.request.UpdatePostRequest;
import com.project.blog_be.domain.dto.request.CreatePostRequestDto;
import com.project.blog_be.domain.dto.request.UpdatePostRequestDto;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.UserEntity;
import com.project.blog_be.mappers.PostMapper;
import com.project.blog_be.service.PostService;
import com.project.blog_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPost(@RequestParam(required = false)UUID categoryId, @RequestParam(required = false)UUID tagId) {
        List<PostEntity> postEntities = postService.getAllPost(categoryId, tagId);
        List<PostDto> list = postEntities.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        UserEntity loggedInUser = userService.getUserById(userId);
        List<PostEntity> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> list = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto, @RequestAttribute UUID userId) {
        log.debug("CreatePostRequest: {}", createPostRequestDto);
        log.debug("PostStatus: {}", createPostRequestDto.getPostStatus());
        UserEntity loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        PostEntity post = postService.createPost(loggedInUser, createPostRequest);
        PostDto createdPostDto = postMapper.toDto(post);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable UUID id, @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        PostEntity updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto postDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(postDto);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        PostEntity post = postService.getPost(id);
        return ResponseEntity.ok(postMapper.toDto(post));
    }
}
