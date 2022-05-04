package com.writemaster.platform.controller;

import com.writemaster.platform.dto.LikeDTO;
import com.writemaster.platform.service.LikeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/likes/")
public class LikeController {
  private final LikeService likeService;

  public LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  @ApiOperation(value = "Like a post", response = LikeDTO.class)
  @ApiResponse(code = 200, message = "Post liked")
  @PostMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<LikeDTO> likePost(@PathVariable Integer authorId,
                                          @PathVariable Integer postId) {
    LikeDTO liked = likeService.likePost(authorId, postId);
    return ResponseEntity.ok(liked);
  }

  @ApiOperation(value = "Unlike a post", response = LikeDTO.class)
  @ApiResponse(code = 200, message = "Post unliked")
  @DeleteMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<LikeDTO> unlikePost(@PathVariable Integer authorId,
                                            @PathVariable Integer postId) {
    LikeDTO unliked = likeService.unlikePost(authorId, postId);
    return ResponseEntity.ok(unliked);
  }

  @ApiOperation(value = "Get a like if it exists", response = LikeDTO.class)
  @ApiResponse(code = 200, message = "Get a certain like")
  @GetMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<LikeDTO> getLike(@PathVariable Integer authorId,
                                            @PathVariable Integer postId) {
    LikeDTO like = likeService.getLikeByAuthorAndPost(authorId, postId);
    return ResponseEntity.ok(like);
  }
}
