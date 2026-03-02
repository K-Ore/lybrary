package com.backend.lybrary.service.file;

import com.backend.lybrary.dto.file.*;
import com.backend.lybrary.entity.*;
import com.backend.lybrary.mapper.FileMapper;
import com.backend.lybrary.repository.FileRepository;
import com.backend.lybrary.repository.UserRepository;
import com.backend.lybrary.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileResponse create(FileCreateRequest request) {

        User user = getCurrentUser();

        FileEntity file = FileEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .favorite(false)
                .status(FileStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        return FileMapper.toResponse(fileRepository.save(file));
    }

    public Page<FileResponse> getAllActive(int page, int size) {

        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return fileRepository
                .findByUserIdAndStatus(user.getId(), FileStatus.ACTIVE, pageable)
                .map(FileMapper::toResponse);
    }

    public Page<FileResponse> getFavorites(int page, int size) {

        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "favoriteAt")
        );

        return fileRepository
                .findByUserIdAndStatusAndFavoriteTrue(
                        user.getId(),
                        FileStatus.ACTIVE,
                        pageable
                )
                .map(FileMapper::toResponse);
    }

    public FileResponse update(Long fileId, FileUpdateRequest request) {

        User user = getCurrentUser();

        FileEntity file = fileRepository
                .findByIdAndUserId(fileId, user.getId())
                .orElseThrow(() -> new RuntimeException("File not found"));

        file.setTitle(request.getTitle());
        file.setContent(request.getContent());
        file.setUpdatedAt(LocalDateTime.now());

        return FileMapper.toResponse(fileRepository.save(file));
    }

    public void toggleFavorite(Long fileId) {

        User user = getCurrentUser();

        FileEntity file = fileRepository
                .findByIdAndUserId(fileId, user.getId())
                .orElseThrow(() -> new RuntimeException("File not found"));

        file.setFavorite(!file.isFavorite());
        file.setFavoriteAt(file.isFavorite() ? LocalDateTime.now() : null);

        fileRepository.save(file);
    }

    public void moveToTrash(Long fileId) {

        User user = getCurrentUser();

        FileEntity file = fileRepository
                .findByIdAndUserId(fileId, user.getId())
                .orElseThrow(() -> new RuntimeException("File not found"));

        file.setStatus(FileStatus.TRASHED);
        file.setTrashedAt(LocalDateTime.now());

        fileRepository.save(file);
    }

    public Page<FileResponse> search(
            String keyword,
            int page,
            int size,
            String sortBy
    ) {
        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, sortBy)
        );

        Page<FileEntity> result =
                (keyword == null || keyword.isBlank())
                        ? fileRepository.findByUserIdAndStatus(
                        user.getId(),
                        FileStatus.ACTIVE,
                        pageable
                )
                        : fileRepository.search(
                        user.getId(),
                        FileStatus.ACTIVE,
                        keyword,
                        pageable
                );

        return result.map(FileMapper::toResponse);
    }

    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        if (email == null) {
            throw new RuntimeException("Unauthenticated request");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
