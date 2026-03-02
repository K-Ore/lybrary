package com.backend.lybrary.service.admin;

import com.backend.lybrary.dto.trash.TrashFileResponse;
import com.backend.lybrary.entity.FileEntity;
import com.backend.lybrary.entity.FileStatus;
import com.backend.lybrary.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminFileService {

    private final FileRepository fileRepository;

    public Page<TrashFileResponse> getAllTrashedFiles(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "trashedAt")
        );

        return fileRepository
                .findByStatus(FileStatus.TRASHED, pageable)
                .map(this::toTrashResponse);
    }

    public void permanentlyDelete(Long fileId) {

        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException("File not found")
                );

        if (file.getStatus() != FileStatus.TRASHED) {
            throw new RuntimeException("Only trashed files can be permanently deleted");
        }

        fileRepository.delete(file);

        log.warn("ADMIN permanently deleted file with id {}", fileId);
    }

    private TrashFileResponse toTrashResponse(FileEntity file) {
        return TrashFileResponse.builder()
                .id(file.getId())
                .title(file.getTitle())
                .content(file.getContent())
                .trashedAt(file.getTrashedAt())
                .build();
    }
}
