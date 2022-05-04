package com.writemaster.platform.mapper;

import com.writemaster.platform.dto.LikeDTO;
import com.writemaster.platform.entity.Like;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LikeMapper {
  public LikeDTO fromEntity(Like like) {
    LikeDTO likeDTO = new LikeDTO();
    if (Optional.ofNullable(like).isEmpty()) {
      return likeDTO;
    }
    likeDTO.setAuthorId(like.getAuthor().getId());
    likeDTO.setPostId(like.getPost().getId());
    likeDTO.setAuthorName(like.getAuthor().getRealName());
    likeDTO.setLiked(like.getLiked());
    return likeDTO;
  }
}
