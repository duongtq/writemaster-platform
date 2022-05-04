package com.writemaster.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportDTO {
  private Integer reporterId;
  private String reporterName;
  private Integer commentId;
  private String commentContent;
  private String reason;
  private String note;
  private String stateOfReviewal;
  private Date reviewedOn;
}
