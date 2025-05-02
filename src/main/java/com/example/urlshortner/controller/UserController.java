package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.UserRequest;
import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.dto.response.UserResponse;
import com.example.urlshortner.service.UserService;
import com.example.urlshortner.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseHandler.create("User created successfully", userResponse, "/user");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequest userRequest
    ) {
        UserResponse userResponse = userService.updateUser(userId, userRequest);
        return ResponseHandler.success(
                "User updated successfully",
                userResponse,
                HttpStatus.OK,
                "/user/" + userId
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseHandler.success(
                "User retrieved successfully",
                userResponse,
                HttpStatus.OK,
                "/user/" + userId
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseHandler.success(
                "Users retrieved successfully",
                users,
                HttpStatus.OK,
                "/user"
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseHandler.success(
                "User deleted successfully",
                null,
                HttpStatus.OK,
                "/user/" + userId
        );
    }
}