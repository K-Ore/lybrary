package com.backend.lybrary.service.dashboard;

import com.backend.lybrary.dto.dashboard.UserDashboardResponse;
import com.backend.lybrary.entity.FileEntity;
import com.backend.lybrary.entity.FileStatus;
import com.backend.lybrary.entity.User;
import com.backend.lybrary.repository.FileRepository;
import com.backend.lybrary.repository.UserRepository;
import com.backend.lybrary.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDashboardService {

    private static final int SAFE_LIMIT = 1000;

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public UserDashboardResponse getDashboard() {

        User user = userRepository
                .findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        Pageable limitAll = PageRequest.of(0, SAFE_LIMIT);
        Pageable topFive = PageRequest.of(0, 5);

        List<FileEntity> activeFiles =
                fileRepository
                        .findByUserIdAndStatus(userId, FileStatus.ACTIVE, limitAll)
                        .getContent();

        List<FileEntity> trashedFiles =
                fileRepository
                        .findByUserIdAndStatus(userId, FileStatus.TRASHED, limitAll)
                        .getContent();

        long favoriteCount =
                activeFiles.stream()
                        .filter(FileEntity::isFavorite)
                        .count();

        List<UserDashboardResponse.RecentNote> recentNotes =
                fileRepository
                        .findByUserIdAndStatus(userId, FileStatus.ACTIVE, topFive)
                        .getContent()
                        .stream()
                        .sorted((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()))
                        .map(f -> UserDashboardResponse.RecentNote.builder()
                                .id(f.getId())
                                .title(f.getTitle())
                                .updatedAt(f.getUpdatedAt().toString())
                                .favorite(f.isFavorite())
                                .build())
                        .toList();

        return UserDashboardResponse.builder()
                .totalNotes(activeFiles.size() + trashedFiles.size())
                .activeNotes(activeFiles.size())
                .trashedNotes(trashedFiles.size())
                .favoriteNotes(favoriteCount)
                .recentNotes(recentNotes)
                .build();
    }
}
