package com.backend.lybrary.controller.file;

import com.backend.lybrary.dto.trash.TrashFileResponse;
import com.backend.lybrary.service.file.FileTrashService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files/trash")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FileTrashController {

    private final FileTrashService trashService;

    @GetMapping
    public ResponseEntity<Page<TrashFileResponse>> getTrash(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                trashService.getTrash(page, size)
        );
    }

    @PutMapping("/{fileId}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long fileId) {
        trashService.restore(fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{fileId}/permanent")
    public ResponseEntity<Void> permanentlyDelete(@PathVariable Long fileId) {
        trashService.permanentlyDelete(fileId);
        return ResponseEntity.noContent().build();
    }
}
