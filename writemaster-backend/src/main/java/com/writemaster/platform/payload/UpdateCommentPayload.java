package com.writemaster.platform.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentPayload {
  private Integer commentId;
  private String content;
  private Date lastEditedOn;
}
