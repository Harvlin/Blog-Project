package com.project.blog_be.service.impl;

import com.project.blog_be.domain.entity.TagEntity;
import com.project.blog_be.repository.TagRepository;
import com.project.blog_be.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagEntity> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    public List<TagEntity> createTags(Set<String> tagNames) {
        List<TagEntity> existingTags = tagRepository.findByNameInIgnoreCase(tagNames);

        Set<String> existingTagNames = existingTags.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet());

        List<TagEntity> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> TagEntity.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<TagEntity> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) throw new IllegalStateException("Cannot delete tag with post");
            tagRepository.deleteById(id);
        });
    }

    @Override
    public TagEntity getTagById(UUID id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id " + id));
    }

    @Override
    public List<TagEntity> getTagByIds(Set<UUID> id) {
        List<TagEntity> foundTag = tagRepository.findAllById(id);
        if (foundTag.size() != id.size()) {
            throw new EntityNotFoundException("Not all specified tag IDs exist");
        }
        return foundTag;
    }
}
