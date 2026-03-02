package com.backend.lybrary.repository;

import com.backend.lybrary.entity.FileEntity;
import com.backend.lybrary.entity.FileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query("""
        SELECT f FROM FileEntity f
        WHERE f.user.id = :userId
          AND f.status = :status
          AND (
                LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(f.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
    """)
    Page<FileEntity> search(
            Long userId,
            FileStatus status,
            String keyword,
            Pageable pageable
    );

    Page<FileEntity> findByUserIdAndStatus(
            Long userId,
            FileStatus status,
            Pageable pageable
    );

    Page<FileEntity> findByUserIdAndStatusAndFavoriteTrue(
            Long userId,
            FileStatus status,
            Pageable pageable
    );

    Optional<FileEntity> findByIdAndUserId(Long fileId, Long userId);


    Page<FileEntity> findByStatus(
            FileStatus status,
            Pageable pageable
    );


    long countByStatus(FileStatus status);

    @Query("""
        SELECT COUNT(f) FROM FileEntity f
        WHERE f.trashedAt IS NOT NULL
          AND f.trashedAt >= :since
    """)
    long countDeletedSince(LocalDateTime since);

    @Modifying
    @Query("""
    DELETE FROM FileEntity f
    WHERE f.status = :status
      AND f.trashedAt < :cutoff
""")
    int deleteOldTrashedFiles(
            FileStatus status,
            LocalDateTime cutoff
    );


}
