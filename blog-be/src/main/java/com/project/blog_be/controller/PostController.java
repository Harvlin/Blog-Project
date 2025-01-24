package com.project.blog_be.controller;

import com.project.blog_be.domain.dto.PostDto;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.UserEntity;
import com.project.blog_be.mappers.PostMapper;
import com.project.blog_be.service.PostService;
import com.project.blog_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
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

}
