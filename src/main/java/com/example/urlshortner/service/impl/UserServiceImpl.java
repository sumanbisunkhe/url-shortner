package com.example.urlshortner.service.impl;


import com.example.urlshortner.dto.request.UserRequest;
import com.example.urlshortner.dto.response.UserResponse;
import com.example.urlshortner.exception.DuplicateEmailException;
import com.example.urlshortner.exception.DuplicateUsernameException;
import com.example.urlshortner.exception.ResourceNotFoundException;
import com.example.urlshortner.mapper.UserMapper;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.UserRepository;
import com.example.urlshortner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateUsernameException("Username '" + userRequest.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateEmailException("Email '" + userRequest.getEmail() + "' is already in use");
        }

        try {
            User user = userMapper.toEntity(userRequest);
            User savedUser = userRepository.save(user);
            return userMapper.toResponse(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUsernameException("Database constraint violation: " + ex.getRootCause().getMessage());
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (userRequest.getUsername() != null &&
                !user.getUsername().equals(userRequest.getUsername()) &&
                userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateUsernameException("Username '" + userRequest.getUsername() + "' is already taken");
        }

        if (userRequest.getEmail() != null &&
                !user.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateEmailException("Email '" + userRequest.getEmail() + "' is already in use");
        }

        if (userRequest.getUsername() != null) user.setUsername(userRequest.getUsername());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(userMapper.encodePassword(userRequest.getPassword()));
        }
        if (userRequest.getRole() != null) user.setRole(userRequest.getRole());

        try {
            User updatedUser = userRepository.save(user);
            return userMapper.toResponse(updatedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUsernameException("Database error during update: " + ex.getRootCause().getMessage());
        }
    }


    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toResponse(user);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}