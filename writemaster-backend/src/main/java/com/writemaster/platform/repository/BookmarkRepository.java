package com.writemaster.platform.repository;

import com.writemaster.platform.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Date> {
  Bookmark findBookmarkByAuthorIdAndPostIdAndBookmarkedOn(Integer authorId, Integer postId, Date bookmarkedOn);

  List<Bookmark> getAllByAuthorIdOrderByBookmarkedOnAsc(Integer authorId);

  Bookmark findBookmarkByAuthorIdAndPostId(Integer authorId, Integer postId);
}
