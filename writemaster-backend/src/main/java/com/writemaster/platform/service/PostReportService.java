package com.writemaster.platform.service;

import com.writemaster.platform.dto.PostReportDTO;
import com.writemaster.platform.payload.PostReportPayload;
import com.writemaster.platform.payload.ReviewPostReportPayload;

import java.util.Set;

public interface PostReportService {
  PostReportDTO reportPost(PostReportPayload postReportPayload);
  PostReportDTO reviewPostReport(ReviewPostReportPayload reviewPostReportPayload);
  Set<PostReportDTO> getAllPostReports();
}
