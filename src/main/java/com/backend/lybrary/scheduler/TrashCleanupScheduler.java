package com.backend.lybrary.scheduler;

import com.backend.lybrary.entity.FileEntity;
import com.backend.lybrary.entity.FileStatus;
import com.backend.lybrary.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrashCleanupScheduler {

    private final FileRepository fileRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOldTrash() {

        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);

        int deletedCount = fileRepository.deleteOldTrashedFiles(
                FileStatus.TRASHED,
                cutoff
        );

    }
}

