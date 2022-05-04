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
public class CreatePostPayload {
  private Integer authorId;
  private String title;
  private String description;
  private String content;
  private Date createdDate;
}
