package com.example.urlshortner.dto.request;

import com.example.urlshortner.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotNull(message = "Role must be provided.")
    private Role role;
}