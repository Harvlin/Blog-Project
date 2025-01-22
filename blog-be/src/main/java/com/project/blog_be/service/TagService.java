package com.project.blog_be.service;

import com.project.blog_be.domain.entity.TagEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {

    List<TagEntity> getTags();
    List<TagEntity> createTags(Set<String> tagNames);
    void deleteTag(UUID id);
    TagEntity getTagById(UUID id);

}
