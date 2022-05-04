package com.writemaster.platform.service;

import com.writemaster.platform.dto.CommentReportDTO;
import com.writemaster.platform.payload.CommentReportPayload;
import com.writemaster.platform.payload.ReviewCommentReportPayload;

import java.util.Set;

public interface CommentReportService {
  CommentReportDTO reportComment(CommentReportPayload commentReportPayload);
  CommentReportDTO reviewCommentReport(ReviewCommentReportPayload reviewCommentReportPayload);
  Set<CommentReportDTO> getAllCommentReports();
}
