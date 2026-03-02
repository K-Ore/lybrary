package com.backend.lybrary.dto.trash;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrashFileResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime trashedAt;
}
