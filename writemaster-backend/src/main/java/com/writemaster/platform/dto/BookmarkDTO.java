package com.writemaster.platform.dto;


import com.writemaster.platform.common.BookmarkState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
  private Integer authorId;
  private String authorName;
  private Integer postId;
  private String postTitle;
  private Date bookmarkedOn;
  private BookmarkState status;
}
