package com.writemaster.platform.controller;

import com.writemaster.platform.dto.CommentDTO;
import com.writemaster.platform.payload.CreateCommentPayload;
import com.writemaster.platform.payload.DeleteCommentPayload;
import com.writemaster.platform.payload.UpdateCommentPayload;
import com.writemaster.platform.service.CommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/comments/")
public class CommentController {
  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @ApiOperation(value = "Get comments of author", response = CommentDTO.class)
  @ApiResponse(code = 200, message = "OK")
  @GetMapping(value = "/author/{authorId}")
  public ResponseEntity<Set<CommentDTO>> getCommentsByAuthorId(@PathVariable Integer authorId) {
    return ResponseEntity.ok(commentService.findCommentsByAuthorId(authorId));
  }

  @ApiOperation(value = "Get comments of post", response = CommentDTO.class)
  @GetMapping(value = "/post/{postId}")
  public ResponseEntity<Set<CommentDTO>> getCommentsByPostId(@PathVariable Integer postId) {
    return ResponseEntity.ok(commentService.findCommentsByPostId(postId));
  }

  @ApiOperation(value = "Make comment on a post", response = CommentDTO.class)
  @ApiResponse(code = 201, message = "Comment added")
  @PostMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<CommentDTO> addComment(@PathVariable Integer authorId,
                                               @PathVariable Integer postId,
                                               @RequestBody CreateCommentPayload payload) {
    CommentDTO newComment = commentService.addComment(authorId, postId, payload);
    return new ResponseEntity<>(newComment, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update comment on a post", response = CommentDTO.class)
  @ApiResponse(code = 200, message = "Comment updated")
  @PutMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer authorId,
                                                  @PathVariable Integer postId,
                                                  @RequestBody UpdateCommentPayload payload) {
    CommentDTO updatedComment = commentService.updateComment(authorId, postId, payload);
    return ResponseEntity.ok(updatedComment);
  }

  @ApiOperation(value = "Delete comment on a post")
  @ApiResponse(code = 200, message = "Comment deleted")
  @DeleteMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<CommentDTO> deleteComment(@PathVariable Integer authorId,
                                                  @PathVariable Integer postId,
                                                  @RequestBody DeleteCommentPayload payload) {
    commentService.deleteComment(authorId, postId, payload);
    return ResponseEntity.ok().build();
  }

}
