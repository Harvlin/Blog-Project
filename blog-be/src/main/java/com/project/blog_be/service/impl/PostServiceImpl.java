package com.project.blog_be.service.impl;

import com.project.blog_be.domain.PostStatus;
import com.project.blog_be.domain.dto.CreatePostRequest;
import com.project.blog_be.domain.entity.CategoryEntity;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.TagEntity;
import com.project.blog_be.domain.entity.UserEntity;
import com.project.blog_be.repository.PostRepository;
import com.project.blog_be.service.CategoryService;
import com.project.blog_be.service.PostService;
import com.project.blog_be.service.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    public List<PostEntity> getAllPost(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            CategoryEntity category = categoryService.getCategoryById(categoryId);
            TagEntity tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        if (categoryId != null) {
            CategoryEntity category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tagId != null) {
            TagEntity tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<PostEntity> getDraftPosts(UserEntity user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    public PostEntity createPost(UserEntity userEntity, CreatePostRequest createPostRequest) {
        PostEntity newPost = new PostEntity();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(userEntity);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        CategoryEntity categoryEntity = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(categoryEntity);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<TagEntity> tags = tagService.getTagById(tagIds);
        newPost.setTags(new HashSet<>(tags));
        return postRepository.save(newPost);
    }
}
