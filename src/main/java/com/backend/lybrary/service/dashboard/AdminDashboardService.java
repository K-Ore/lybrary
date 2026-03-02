package com.backend.lybrary.service.dashboard;

import com.backend.lybrary.dto.dashboard.AdminDashboardResponse;
import com.backend.lybrary.entity.FileStatus;
import com.backend.lybrary.repository.FileRepository;
import com.backend.lybrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public AdminDashboardResponse getDashboard() {

        long totalUsers = userRepository.count();
        long totalNotes = fileRepository.count();

        long trashedNotes = fileRepository.countByStatus(FileStatus.TRASHED);

        long deletedLast30Days =
                fileRepository.countDeletedSince(
                        LocalDateTime.now().minusDays(30)
                );

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalNotes(totalNotes)
                .trashedNotes(trashedNotes)
                .deletedLast30Days(deletedLast30Days)
                .build();
    }
}
