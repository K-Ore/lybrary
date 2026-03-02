package com.backend.lybrary.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDashboardResponse {

    private long totalNotes;
    private long activeNotes;
    private long trashedNotes;
    private long favoriteNotes;

    private List<RecentNote> recentNotes;

    @Data
    @Builder
    public static class RecentNote {
        private Long id;
        private String title;
        private String updatedAt;
        private boolean favorite;
    }
}
