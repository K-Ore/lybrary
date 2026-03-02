package com.backend.lybrary.controller.admin;

import com.backend.lybrary.dto.admin.UserAdminResponse;
import com.backend.lybrary.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<Page<UserAdminResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                adminUserService.getAllUsers(page, size)
        );
    }

    @PutMapping("/{userId}/block")
    public ResponseEntity<Void> block(@PathVariable Long userId) {
        adminUserService.blockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/unblock")
    public ResponseEntity<Void> unblock(@PathVariable Long userId) {
        adminUserService.unblockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
