package com.writemaster.platform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
public class PostDTO {

  private String author;

  private Integer id;

  private String title;

  private String description;

  private String content;
  
  private Integer numberOfLikes;
  
  private Integer numberOfComments;
  
  private Integer numberOfBookmarks;

  private Set<LikeDTO> likes;

  private Set<CommentDTO> comments;

  private Set<BookmarkDTO> bookmarks;

  private Date createdDate;

  private int authorId;
  
  private String authorName;

  private String avatarUrl;
}
