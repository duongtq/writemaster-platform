package com.writemaster.platform.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReportPayload {
  private Integer reporterId;
  private Integer postId;
  private String reason;
  private String stateOfReviewal;
}
