package com.writemaster.platform.mapper;

import com.writemaster.platform.common.BookmarkState;
import com.writemaster.platform.dto.BookmarkDTO;
import com.writemaster.platform.entity.Bookmark;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {
  public BookmarkDTO fromEntity(Bookmark entity, BookmarkState bookmarkState) {
    BookmarkDTO bookmarkDTO = new BookmarkDTO();
    bookmarkDTO.setAuthorId(entity.getAuthor().getId());
    bookmarkDTO.setAuthorName(entity.getAuthor().getRealName());
    bookmarkDTO.setPostId(entity.getPost().getId());
    bookmarkDTO.setPostTitle(entity.getPost().getTitle());
    bookmarkDTO.setBookmarkedOn(entity.getBookmarkedOn());
    bookmarkDTO.setStatus(bookmarkState);
    return bookmarkDTO;
  }

  public BookmarkDTO fromEntity(Bookmark entity) {
    BookmarkDTO bookmarkDTO = new BookmarkDTO();
    bookmarkDTO.setAuthorId(entity.getAuthor().getId());
    bookmarkDTO.setAuthorName(entity.getAuthor().getRealName());
    bookmarkDTO.setPostId(entity.getPost().getId());
    bookmarkDTO.setPostTitle(entity.getPost().getTitle());
    bookmarkDTO.setBookmarkedOn(entity.getBookmarkedOn());
    return bookmarkDTO;
  }
}
