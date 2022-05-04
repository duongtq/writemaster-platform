package com.writemaster.platform.repository;

import com.writemaster.platform.entity.CommentPKReport;
import com.writemaster.platform.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, CommentPKReport> {
  CommentReport findByReporterIdAndCommentId(Integer authorId, Integer postId);
}
