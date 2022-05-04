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
public class CommentDTO {
  private Integer commentId;
  private Integer authorId;
  private String author;
  private Integer postId;
  private String postTitle;
  private String content;
  private Date createdOn;
  private Date lastEditedOn;
  private String authorAvatarUrl;
}
