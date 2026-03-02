package com.backend.lybrary.service.admin;

import com.backend.lybrary.dto.admin.UserAdminResponse;
import com.backend.lybrary.entity.User;
import com.backend.lybrary.exception.InvalidOperationException;
import com.backend.lybrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final UserRepository userRepository;

    public Page<UserAdminResponse> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return userRepository.findAll(pageable)
                .map(this::map);
    }

    public void blockUser(Long userId) {
        User user = getUser(userId);

        if (user.isDeleted()) {
            throw new InvalidOperationException("Cannot block deleted user");
        }

        user.setBlocked(true);
        userRepository.save(user);

        log.warn("ADMIN blocked user {}", userId);
    }

    public void unblockUser(Long userId) {
        User user = getUser(userId);

        if (user.isDeleted()) {
            throw new InvalidOperationException("Cannot unblock deleted user");
        }

        user.setBlocked(false);
        userRepository.save(user);

        log.warn("ADMIN unblocked user {}", userId);
    }

    public void deleteUser(Long userId) {
        User user = getUser(userId);

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role ->
                        role.equalsIgnoreCase("ADMIN") ||
                                role.equalsIgnoreCase("ROLE_ADMIN")
                );

        if (isAdmin) {
            throw new InvalidOperationException("Cannot delete admin user");
        }

        if (user.isDeleted()) {
            throw new InvalidOperationException("User already deleted");
        }

        user.setDeleted(true);
        userRepository.save(user);

        log.warn("ADMIN deleted user {}", userId);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidOperationException("User not found"));
    }

    private UserAdminResponse map(User user) {
        return UserAdminResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .blocked(user.isBlocked())
                .deleted(user.isDeleted())
                .build();
    }
}
