package com.writemaster.platform.mapper;

import com.writemaster.platform.dto.PostDTO;
import com.writemaster.platform.entity.Post;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PostMapper {
  private final LikeMapper likeMapper;
  private final CommentMapper commentMapper;
  private final BookmarkMapper bookmarkMapper;

  public PostMapper(LikeMapper likeMapper, CommentMapper commentMapper, BookmarkMapper bookmarkMapper) {
    this.likeMapper = likeMapper;
    this.commentMapper = commentMapper;
    this.bookmarkMapper = bookmarkMapper;
  }

  public PostDTO fromNewEntity(Post newEntity) {
    PostDTO dto = new PostDTO();
    dto.setAuthor(newEntity.getAuthor().getUsername());
    dto.setAuthorId(newEntity.getAuthor().getId());
    dto.setId(newEntity.getId());
    dto.setTitle(newEntity.getTitle());
    dto.setContent(newEntity.getContent());
    dto.setDescription(newEntity.getDescription());
    dto.setCreatedDate(newEntity.getCreatedDate());
    dto.setAuthorName(newEntity.getAuthor().getRealName());
    dto.setNumberOfLikes(newEntity.getNumberOfLikes());
    dto.setNumberOfComments(newEntity.getNumberOfComments());
    dto.setNumberOfBookmarks(newEntity.getNumberOfBookmarks());
    return dto;
  }

  public PostDTO fromEntity(Post entity) {
    PostDTO dto = new PostDTO();
    dto.setAuthor(entity.getAuthor().getUsername());
    dto.setAuthorId(entity.getAuthor().getId());
    dto.setAvatarUrl(entity.getAuthor().getAvatarUrl());
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setContent(entity.getContent());
    dto.setDescription(entity.getDescription());
    dto.setCreatedDate(entity.getCreatedDate());
    dto.setAuthorName(entity.getAuthor().getRealName());
    dto.setNumberOfLikes(entity.getNumberOfLikes());
    dto.setNumberOfComments(entity.getNumberOfComments());
    dto.setNumberOfBookmarks(entity.getNumberOfBookmarks());
    dto.setLikes(entity.getLikes().stream().map(likeMapper::fromEntity).collect(Collectors.toSet()));
    dto.setComments(entity.getComments().stream().map(commentMapper::fromEntity).collect(Collectors.toSet()));
    dto.setBookmarks(entity.getBookmarks().stream().map(bookmarkMapper::fromEntity).collect(Collectors.toSet()));
    return dto;
  }

  public Post fromDTO(PostDTO payload) {
    return fromDTO(new Post(), payload);
  }

  public Post fromDTO(Post entity, PostDTO payload) {
    entity.setTitle(payload.getTitle());
    entity.setContent(payload.getContent());
    entity.setDescription(payload.getDescription());
    entity.setCreatedDate(payload.getCreatedDate());
    return entity;
  }
}
