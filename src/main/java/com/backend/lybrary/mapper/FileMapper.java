package com.backend.lybrary.mapper;

import com.backend.lybrary.dto.file.FileResponse;
import com.backend.lybrary.entity.FileEntity;

public class FileMapper {

    private FileMapper() {
    }

    public static FileResponse toResponse(FileEntity file) {

        if (file == null) {
            return null;
        }

        return FileResponse.builder()
                .id(file.getId())
                .title(file.getTitle())
                .content(file.getContent())
                .status(file.getStatus().name())
                .favorite(file.isFavorite())
                .favoriteAt(file.getFavoriteAt())
                .trashedAt(file.getTrashedAt())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}
