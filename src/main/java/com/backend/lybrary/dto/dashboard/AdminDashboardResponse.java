package com.backend.lybrary.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalNotes;
    private long trashedNotes;
    private long deletedLast30Days;
}
