package com.writemaster.platform.service;

import com.writemaster.platform.dto.CommentDTO;
import com.writemaster.platform.payload.CreateCommentPayload;
import com.writemaster.platform.payload.DeleteCommentPayload;
import com.writemaster.platform.payload.UpdateCommentPayload;

import java.util.Set;

public interface CommentService {

  Set<CommentDTO> findCommentsByAuthorId(Integer authorId);

  Set<CommentDTO> findCommentsByPostId(Integer postId);

  CommentDTO addComment(Integer authorId, Integer postId, CreateCommentPayload creationPayload);

  void deleteComment(Integer authorId, Integer postId, DeleteCommentPayload deletionPayload);

  CommentDTO updateComment(Integer authorId, Integer postId, UpdateCommentPayload creationPayload);
}
