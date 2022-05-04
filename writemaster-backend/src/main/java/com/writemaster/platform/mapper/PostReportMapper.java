package com.writemaster.platform.mapper;

import com.writemaster.platform.dto.PostReportDTO;
import com.writemaster.platform.entity.PostReport;
import org.springframework.stereotype.Component;

@Component
public class PostReportMapper {
  public PostReportDTO fromEntity(PostReport entity) {
    PostReportDTO postReportDTO = new PostReportDTO();
    postReportDTO.setReporterId(entity.getReporter().getId());
    postReportDTO.setReporterName(entity.getReporter().getUsername());
    postReportDTO.setPostId(entity.getPost().getId());
    postReportDTO.setPostTitle(entity.getPost().getTitle());
    postReportDTO.setNote(entity.getNote());
    postReportDTO.setReason(entity.getReason());
    postReportDTO.setStateOfReviewal(entity.getStateOfReviewal());
    postReportDTO.setReviewedOn(entity.getReviewedOn());
    return postReportDTO;
  }
}
