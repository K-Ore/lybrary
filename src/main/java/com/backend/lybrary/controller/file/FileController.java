package com.backend.lybrary.controller.file;

import com.backend.lybrary.dto.file.*;
import com.backend.lybrary.service.file.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileResponse> create(
            @Valid @RequestBody FileCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.create(request));
    }


    @GetMapping
    public ResponseEntity<Page<FileResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                fileService.getAllActive(page, size)
        );
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<FileResponse>> getFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                fileService.getFavorites(page, size)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FileResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        return ResponseEntity.ok(
                fileService.search(keyword, page, size, sortBy)
        );
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<FileResponse> update(
            @PathVariable Long fileId,
            @Valid @RequestBody FileUpdateRequest request
    ) {
        return ResponseEntity.ok(
                fileService.update(fileId, request)
        );
    }

    @PutMapping("/{fileId}/favorite")
    public ResponseEntity<Void> toggleFavorite(@PathVariable Long fileId) {
        fileService.toggleFavorite(fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> delete(@PathVariable Long fileId) {
        fileService.moveToTrash(fileId);
        return ResponseEntity.noContent().build();
    }

}
