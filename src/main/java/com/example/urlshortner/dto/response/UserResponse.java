package com.example.urlshortner.dto.response;

import com.example.urlshortner.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private String createdAt;
    private String updatedAt;
}