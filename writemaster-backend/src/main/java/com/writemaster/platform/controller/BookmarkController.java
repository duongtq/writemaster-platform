package com.writemaster.platform.controller;

import com.writemaster.platform.dto.BookmarkDTO;
import com.writemaster.platform.service.BookmarkService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/bookmarks")
public class BookmarkController {
  private final BookmarkService bookmarkService;

  public BookmarkController(BookmarkService bookmarkService) {
    this.bookmarkService = bookmarkService;
  }

  @ApiOperation(value = "Get all bookmarked posts of an author")
  @ApiResponse(code = 200, message = "Fetched all bookmarked posts of an author")
  @GetMapping(value = "/author/{authorId}")
  public ResponseEntity<List<BookmarkDTO>> getBookmarkedPostsOfAuthor(@PathVariable Integer authorId) {
    List<BookmarkDTO> bookmarkList = bookmarkService.getAllBookmarkedPostsByAuthorId(authorId);
    return ResponseEntity.ok(bookmarkList);
  }

  @ApiOperation(value = "Bookmark a post", code = 201)
  @ApiResponse(code = 201, message = "Post bookmarked")
  @PostMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<BookmarkDTO> bookmarkPost(@PathVariable Integer authorId,
                                               @PathVariable Integer postId) {
    BookmarkDTO bookmarkDTO = bookmarkService.bookmarkPost(authorId, postId);
    return new ResponseEntity<>(bookmarkDTO, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Unbookmark a post")
  @ApiResponse(code = 200, message = "Post unbookmarked")
  @DeleteMapping(value = "/author/{authorId}/post/{postId}")
  public ResponseEntity<BookmarkDTO> unbookmarkPost(@PathVariable Integer authorId,
                                                    @PathVariable Integer postId) {
    BookmarkDTO bookmarkDTO = bookmarkService.unbookmarkPost(authorId, postId);
    return ResponseEntity.ok(bookmarkDTO);
  }
}
