package com.project.blog_be.service.impl;

import com.project.blog_be.domain.PostStatus;
import com.project.blog_be.domain.dto.request.CreatePostRequest;
import com.project.blog_be.domain.dto.request.UpdatePostRequest;
import com.project.blog_be.domain.entity.CategoryEntity;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.TagEntity;
import com.project.blog_be.domain.entity.UserEntity;
import com.project.blog_be.repository.PostRepository;
import com.project.blog_be.service.CategoryService;
import com.project.blog_be.service.PostService;
import com.project.blog_be.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public PostEntity getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));
    }

    @Override
    public List<PostEntity> getAllPost(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            CategoryEntity category = categoryService.getCategoryById(categoryId);
            TagEntity tag = tagService.getTagById(tagId);
            return postRepository.findAllByPostStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        if (categoryId != null) {
            CategoryEntity category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByPostStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tagId != null) {
            TagEntity tag = tagService.getTagById(tagId);
            return postRepository.findAllByPostStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByPostStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<PostEntity> getDraftPosts(UserEntity user) {
        return postRepository.findAllByAuthorAndPostStatus(user, PostStatus.DRAFT);
    }

    @Override
    public PostEntity createPost(UserEntity user, CreatePostRequest createPostRequest) {
        PostEntity newPost = new PostEntity();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setPostStatus(
                createPostRequest.getPostStatus()
        );
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        CategoryEntity category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<TagEntity> tagByIds = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tagByIds));

        return postRepository.save(newPost);
    }

    @Override
    public PostEntity updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        PostEntity existingPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist with id " + id));

        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent();
        existingPost.setContent(postContent);
        existingPost.setPostStatus(updatePostRequest.getPostStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if (!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            CategoryEntity newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(TagEntity::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if (!existingTagIds.equals(updatePostRequestTagIds)) {
            List<TagEntity> tagByIds = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(tagByIds));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        PostEntity post = getPost(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty())
            return 0;
        int countWord = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) countWord / WORDS_PER_MINUTE);
    }
}
