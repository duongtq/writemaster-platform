package com.writemaster.platform.repository;

import com.writemaster.platform.entity.Like;
import com.writemaster.platform.entity.LikePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikePK> {
  Like findByAuthorIdAndPostId(Integer authorId, Integer postId);
}
