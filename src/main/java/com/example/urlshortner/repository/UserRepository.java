package com.example.urlshortner.repository;

import com.example.urlshortner.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(@NotBlank(message = "Username is required.") String username);

    boolean existsByEmail(@NotBlank(message = "Email is required.") @Email String email);
}
