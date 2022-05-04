package com.writemaster.platform.controller;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.dto.CommentReportDTO;
import com.writemaster.platform.dto.PostReportDTO;
import com.writemaster.platform.payload.ReviewPostReportPayload;
import com.writemaster.platform.service.AuthorService;
import com.writemaster.platform.service.CommentReportService;
import com.writemaster.platform.service.PostReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
  private final AuthorService authorService;
  private final PostReportService postReportService;
  private final CommentReportService commentReportService;

  public AdminController(AuthorService authorService, PostReportService postReportService, CommentReportService commentReportService) {
    this.authorService = authorService;
    this.postReportService = postReportService;
    this.commentReportService = commentReportService;
  }

  @ApiOperation(value = "Get all reports about posts")
  @ApiResponse(code = 200, message = "Reports about posts fetched")
  @GetMapping(value = "/reports/posts")
  public ResponseEntity<Set<PostReportDTO>> getAllPostReports() {
    return ResponseEntity.ok(postReportService.getAllPostReports());
  }

  @ApiOperation(value = "Get all about comments")
  @ApiResponse(code = 200, message = "Reports about comments fetched")
  @GetMapping(value = "/reports/comments")
  public ResponseEntity<Set<CommentReportDTO>> getAllCommentReports() {
    return ResponseEntity.ok(commentReportService.getAllCommentReports());
  }

  @ApiOperation(value = "Review a report")
  @ApiResponse(code = 200, message = "Report reviewed")
  @PutMapping(value = "/reports/review")
  public ResponseEntity<PostReportDTO> reviewReport(@RequestBody ReviewPostReportPayload payload) {
    PostReportDTO reviewed = postReportService.reviewPostReport(payload);
    return ResponseEntity.ok(reviewed);
  }

  @ApiOperation(value = "Get all authors")
  @ApiResponse(code = 200, message = "OK")
  @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
    List<AuthorDTO> authors = authorService.getAllAuthors();
    return ResponseEntity.ok(authors);
  }

  @ApiOperation(value = "Get author by id")
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 404, message = "No author with given id")
  })
  @GetMapping(value = "/authors/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable(name = "id") Integer id) {
    AuthorDTO authorDTO = authorService.findAuthorById(id);
    return ResponseEntity.ok(authorDTO);
  }

  @ApiOperation(value = "Get author by username")
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 404, message = "No author with given username")
  })
  @GetMapping(value = "/authors/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDTO> findAuthorByUsername(@PathVariable(name = "username") String username) {
    AuthorDTO authorDTO = authorService.findAuthorByUsername(username);
    return ResponseEntity.ok(authorDTO);
  }
}
