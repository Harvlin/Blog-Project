package com.project.blog_be.mappers;

import com.project.blog_be.domain.dto.request.CreatePostRequest;
import com.project.blog_be.domain.dto.PostDto;
import com.project.blog_be.domain.dto.request.UpdatePostRequest;
import com.project.blog_be.domain.dto.request.CreatePostRequestDto;
import com.project.blog_be.domain.dto.request.UpdatePostRequestDto;
import com.project.blog_be.domain.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "postStatus", source = "postStatus")
    PostDto toDto(PostEntity postEntity);

    @Mapping(source = "postStatus", target = "postStatus")
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    @Mapping(source = "postStatus", target = "postStatus")
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
