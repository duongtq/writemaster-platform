package com.writemaster.platform.mapper;

import com.writemaster.platform.dto.CommentReportDTO;
import com.writemaster.platform.entity.CommentReport;
import org.springframework.stereotype.Component;

@Component
public class CommentReportMapper {
  public CommentReportDTO fromEntity(CommentReport entity) {
    CommentReportDTO commentReportDTO = new CommentReportDTO();
    commentReportDTO.setReporterId(entity.getReporter().getId());
    commentReportDTO.setReporterName(entity.getReporter().getUsername());
    commentReportDTO.setCommentId(entity.getComment().getId());
    commentReportDTO.setCommentContent(entity.getComment().getContent());
    commentReportDTO.setNote(entity.getNote());
    commentReportDTO.setReason(entity.getReason());
    commentReportDTO.setStateOfReviewal(entity.getStateOfReviewal());
    commentReportDTO.setReviewedOn(entity.getReviewedOn());
    return commentReportDTO;
  }
}
