package com.example.urlshortner.service;

import com.example.urlshortner.dto.request.UserRequest;
import com.example.urlshortner.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Long userId, UserRequest userRequest);
    UserResponse getUserById(Long userId);
    Page<UserResponse> getAllUsers(Pageable pageable);
    void deleteUser(Long userId);
}