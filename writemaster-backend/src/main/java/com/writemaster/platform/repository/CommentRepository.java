package com.writemaster.platform.repository;

import com.writemaster.platform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

  @Query(value = "FROM Comment c WHERE c.author.id = :authorId")
  List<Comment> findCommentsByAuthorId(Integer authorId);

  @Query(value = "FROM Comment c WHERE c.post.id = :postId")
  List<Comment> findCommentsByPostId(Integer postId);

  Comment findCommentById(Integer commentId);
  
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM Comment c WHERE c.id = :commentId AND c.author.id = :authorId AND c.post.id = :postId")
  void deleteCommentByIdAndAuthorIdAndPostId(Integer commentId, Integer authorId, Integer postId);
}
