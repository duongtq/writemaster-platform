package com.writemaster.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
  private Integer authorId;
  private String authorName;
  private Integer postId;
  private Boolean liked;
}
