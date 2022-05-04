package com.writemaster.platform.service;

import com.writemaster.platform.dto.BookmarkDTO;

import java.util.Date;
import java.util.List;

public interface BookmarkService {
  BookmarkDTO bookmarkPost(Integer authorId, Integer postId);

  BookmarkDTO unbookmarkPost(Integer authorId, Integer postId, Date sharedOn);

  List<BookmarkDTO> getAllBookmarkedPostsByAuthorId(Integer authorId);

  BookmarkDTO unbookmarkPost(Integer authorId, Integer postId);

}
