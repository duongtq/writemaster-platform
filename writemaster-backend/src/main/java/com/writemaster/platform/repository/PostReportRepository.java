package com.writemaster.platform.repository;

import com.writemaster.platform.entity.PostReport;
import com.writemaster.platform.entity.PostReportPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, PostReportPK> {
  PostReport findByReporterIdAndPostId(Integer authorId, Integer postId);
}
