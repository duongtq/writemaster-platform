package com.writemaster.platform.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportPayload {
  private Integer reporterId;
  private Integer commentId;
  private String reason;
  private String stateOfReviewal;
}
