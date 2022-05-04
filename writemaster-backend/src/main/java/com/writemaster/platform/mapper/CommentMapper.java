package com.writemaster.platform.mapper;

import com.writemaster.platform.dto.CommentDTO;
import com.writemaster.platform.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
  public CommentDTO fromEntity(Comment entity) {
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setCommentId(entity.getId());
    commentDTO.setAuthorId(entity.getAuthor().getId());
    commentDTO.setPostId(entity.getPost().getId());
    commentDTO.setAuthor(entity.getAuthor().getUsername());
    commentDTO.setPostTitle(entity.getPost().getTitle());
    commentDTO.setContent(entity.getContent());
    commentDTO.setCreatedOn(entity.getCreatedOn());
    commentDTO.setLastEditedOn(entity.getLastEditedOn());
    commentDTO.setAuthorAvatarUrl(entity.getAuthor().getAvatarUrl()) ;
    return commentDTO;
  }
}
