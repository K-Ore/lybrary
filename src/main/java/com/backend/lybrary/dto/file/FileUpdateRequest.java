package com.backend.lybrary.dto.file;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
