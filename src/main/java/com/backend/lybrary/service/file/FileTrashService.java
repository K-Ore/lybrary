package com.backend.lybrary.service.file;


import com.backend.lybrary.dto.trash.TrashFileResponse;
import com.backend.lybrary.entity.FileEntity;
import com.backend.lybrary.entity.FileStatus;
import com.backend.lybrary.entity.User;
import com.backend.lybrary.repository.FileRepository;
import com.backend.lybrary.repository.UserRepository;
import com.backend.lybrary.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FileTrashService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public Page<TrashFileResponse> getTrash(int page, int size) {

        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "trashedAt")
        );

        return fileRepository
                .findByUserIdAndStatus(user.getId(), FileStatus.TRASHED, pageable)
                .map(this::toTrashResponse);
    }

    @Transactional
    public void restore(Long fileId) {

        User user = getCurrentUser();

        FileEntity file = fileRepository
                .findByIdAndUserId(fileId, user.getId())
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (file.getStatus() != FileStatus.TRASHED) {
            throw new RuntimeException("File is not in trash");
        }

        file.setStatus(FileStatus.ACTIVE);
        file.setTrashedAt(null);

        fileRepository.save(file);
    }

    @Transactional
    public void permanentlyDelete(Long fileId) {

        User user = getCurrentUser();

        FileEntity file = fileRepository
                .findByIdAndUserId(fileId, user.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "File not found or not owned by user"
                        )
                );

        if (file.getStatus() != FileStatus.TRASHED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only trashed files can be permanently deleted"
            );
        }

        fileRepository.delete(file);
    }


    private TrashFileResponse toTrashResponse(FileEntity file) {
        return TrashFileResponse.builder()
                .id(file.getId())
                .title(file.getTitle())
                .content(file.getContent())
                .trashedAt(file.getTrashedAt())
                .build();
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
