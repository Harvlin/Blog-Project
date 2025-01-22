package com.project.blog_be.service;

import com.project.blog_be.domain.entity.UserEntity;

import java.util.UUID;

public interface UserService {

    UserEntity getUserById(UUID id);

}
