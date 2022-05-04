package com.writemaster.platform.controller;

import com.writemaster.platform.dto.PostDTO;
import com.writemaster.platform.payload.CreatePostPayload;
import com.writemaster.platform.service.PostService;
import com.writemaster.platform.utils.DateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @ApiOperation(value = "Get all posts")
  @ApiResponse(code = 200, message = "OK")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> getAllPosts() {
    List<PostDTO> posts = postService.getAllPostList();
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Get post by id")
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 404, message = "No post with given id")
  })
  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PostDTO> getPost(@PathVariable Integer id) {
    PostDTO post = postService.findPostById(id);
    return ResponseEntity.ok(post);
  }

  @ApiOperation(value = "Get posts by author")
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 404, message = "Not posts of given author")
  })
  @GetMapping(value = "/author/{author}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> findPostByUsername(@PathVariable(name = "author") String author) {
    List<PostDTO> posts = postService.findPostsByAuthor(author);
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Delete post by id")
  @ApiResponses({
          @ApiResponse(code = 200, message = "Post deleted successfully."),
          @ApiResponse(code = 404, message = "No post with given id")
  })
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
    postService.deletePostById(id);
    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "Get posts by title like")
  @GetMapping(value = "/title-like", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> findPostsByTitleLike(@RequestParam(name = "title") String title) {
    List<PostDTO> posts = postService.findByTitleLike(title);
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Get top 10 latest posts")
  @GetMapping(value = "/top10", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> findTop10NewestPosts() {
    List<PostDTO> posts = postService.findTop10NewestPost();
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Get posts created in a particular date")
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 400, message = "Invalid date format")
  })
  @GetMapping(value = "/date/{createdDate}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> findPostsByCreatedDate(@PathVariable(name = "createdDate") String createdDate) {
    LocalDate date;
    try {
      date = DateUtil.convertToDate(createdDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Invalid date format");
    }
    List<PostDTO> posts = postService.findPostByCreatedDate(date);
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Update post by id")
  @ApiResponses({
          @ApiResponse(code = 200, message = "Post updated successfully"),
          @ApiResponse(code = 400, message = "Invalid data format"),
          @ApiResponse(code = 404, message = "No post with given id"),
  })
  @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PostDTO> updatePost(@PathVariable(name = "id") Integer postId, @RequestBody PostDTO payload) {
    PostDTO updatedPost = postService.updatePost(postId, payload);
    return ResponseEntity.ok(updatedPost);
  }

  @ApiOperation(value = "Create post")
  @ApiResponses({
          @ApiResponse(code = 201, message = "Post created successfully"),
          @ApiResponse(code = 400, message = "Invalid data format")
  })
  @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PostDTO> createPost(@RequestBody CreatePostPayload payload) {
    PostDTO newPost = postService.createPost(payload);
    return new ResponseEntity<>(newPost, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Get posts of an author in a particular month")
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 400, message = "Invalid data format"),
          @ApiResponse(code = 404, message = "No author with given id"),
  })
  @GetMapping(value = "/{authorId}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> getPostsOfAuthorInOneMonth(@PathVariable(name = "authorId") Integer authorId, @PathVariable(name = "month") Integer month) {
    List<PostDTO> posts = postService.findPostByAuthorInMonth(authorId, month);
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Search posts by correct title")
  @GetMapping(value = "/title-equal", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PostDTO>> getPostsByTitleEqual(@RequestParam(name = "title") String title) {
    List<PostDTO> posts = postService.findByTitle(title);
    return ResponseEntity.ok(posts);
  }

  @ApiOperation(value = "Find all bookmarked posts of author")
  @GetMapping(value = "/bookmarks/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Set<PostDTO>> getPostsByTitleEqual(@PathVariable(name = "authorId") Integer authorId) {
    Set<PostDTO> bookmarkedPosts = postService.findBookmarkedPostsByAuthorId(authorId);
    return ResponseEntity.ok(bookmarkedPosts);
  }
}
