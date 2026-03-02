package com.backend.lybrary.dto.file;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileResponse {

    private Long id;
    private String title;
    private String content;

    private String status;

    private boolean favorite;
    private LocalDateTime favoriteAt;

    private LocalDateTime trashedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
