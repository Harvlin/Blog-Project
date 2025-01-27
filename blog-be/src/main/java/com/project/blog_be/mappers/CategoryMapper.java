package com.project.blog_be.mappers;

import com.project.blog_be.domain.PostStatus;
import com.project.blog_be.domain.dto.CategoryDto;
import com.project.blog_be.domain.dto.request.CreateCategoryRequest;
import com.project.blog_be.domain.entity.CategoryEntity;
import com.project.blog_be.domain.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(CategoryEntity categoryEntity);

    CategoryEntity toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<PostEntity> posts) {
        if (null == posts) {
            return 0;
        }
        return posts.stream().filter(postEntity -> PostStatus.PUBLISHED.equals(postEntity.getPostStatus())).count();
    }
}
