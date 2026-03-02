package com.backend.lybrary.controller.admin;

import com.backend.lybrary.dto.trash.TrashFileResponse;
import com.backend.lybrary.service.admin.AdminFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/files")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFileController {

    private final AdminFileService adminFileService;

    @GetMapping("/trash")
    public ResponseEntity<Page<TrashFileResponse>> getAllTrashedFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                adminFileService.getAllTrashedFiles(page, size)
        );
    }

    @DeleteMapping("/{fileId}/permanent")
    public ResponseEntity<Void> permanentlyDelete(@PathVariable Long fileId) {
        adminFileService.permanentlyDelete(fileId);
        return ResponseEntity.noContent().build();
    }
}
