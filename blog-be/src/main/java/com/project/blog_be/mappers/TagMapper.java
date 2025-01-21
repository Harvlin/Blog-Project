package com.project.blog_be.mappers;

import com.project.blog_be.domain.PostStatus;
import com.project.blog_be.domain.dto.response.TagResponse;
import com.project.blog_be.domain.entity.PostEntity;
import com.project.blog_be.domain.entity.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse toTagResponse(TagEntity tagEntity);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<PostEntity> posts) {
        if (null == posts) {
            return 0;
        }
        return (int) posts.stream()
                .filter(postEntity -> PostStatus.PUBLISHED.equals(postEntity.getStatus()))
                .count();
    }
}
