package com.writemaster.platform.service;

import com.writemaster.platform.dto.LikeDTO;

public interface LikeService {

  LikeDTO likePost(Integer authorId, Integer postId);

  LikeDTO unlikePost(Integer authorId, Integer postId);

  LikeDTO getLikeByAuthorAndPost(Integer authorId, Integer postId);
}
