package com.example.urlshortner.util;

import com.example.urlshortner.enums.Role;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("admin") && !userRepository.existsByEmail("admin123@gmail.com")) {
            User suman = new User();
            suman.setUsername("admin");
            suman.setEmail("admin123@gmail.com");
            suman.setPassword("$2a$12$fqbadXhqidJSnYJxZtx1yu64SG4zElKVB61fYMiXWL.LqyVozhfN6");
            suman.setRole(Role.ADMIN);
            suman.setCreatedAt(LocalDateTime.now());
            suman.setUpdatedAt(LocalDateTime.now());

            userRepository.save(suman);
        }
    }
}
