package com.devtrack.devtrack_backend.service;

import com.devtrack.devtrack_backend.dto.UserRequest;
import com.devtrack.devtrack_backend.dto.UserResponse;
import com.devtrack.devtrack_backend.entity.Role;
import com.devtrack.devtrack_backend.entity.User;
import com.devtrack.devtrack_backend.exception.IssueNotFoundException;
import com.devtrack.devtrack_backend.exception.UserNotFoundException;
import com.devtrack.devtrack_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return mapToResponse(user);
    }

    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setAvatar(request.getAvatar());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setAvatar(user.getAvatar());

        return response;
    }

    public UserResponse updateRole(
            Long userId,
            Role role) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + userId));

        user.setRole(role);

        User savedUser =
                userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public void deleteUser(Long id) {

            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException(
                        "User not found with id: " + id
                );
            }

            userRepository.deleteById(id);

    }
}
