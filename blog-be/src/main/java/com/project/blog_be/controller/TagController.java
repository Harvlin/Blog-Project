package com.project.blog_be.controller;

import com.project.blog_be.domain.dto.request.CreateTagsRequest;
import com.project.blog_be.domain.dto.response.TagResponse;
import com.project.blog_be.domain.entity.TagEntity;
import com.project.blog_be.mappers.TagMapper;
import com.project.blog_be.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagEntity> tagEntities = tagService.getTags();
        List<TagResponse> tagResponses = tagEntities.stream().map(tagMapper::toTagResponse).toList();

        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest) {
        List<TagEntity> savedTags = tagService.createTags(createTagsRequest.getNames());
        List<TagResponse> createdTagResponse = savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(createdTagResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
