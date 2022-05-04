package com.writemaster.platform.controller;

import com.writemaster.platform.dto.CommentReportDTO;
import com.writemaster.platform.dto.PostReportDTO;
import com.writemaster.platform.payload.CommentReportPayload;
import com.writemaster.platform.payload.PostReportPayload;
import com.writemaster.platform.service.CommentReportService;
import com.writemaster.platform.service.PostReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/reports")
public class ReportController {
  private final PostReportService postReportService;
  private final CommentReportService commentReportService;

  public ReportController(PostReportService postReportService, CommentReportService commentReportService) {
    this.postReportService = postReportService;
    this.commentReportService = commentReportService;
  }

  @ApiOperation(value = "Report a post")
  @ApiResponse(code = 201, message = "Post reported")
  @PostMapping(value = "/post")
  public ResponseEntity<PostReportDTO> reportPost(@RequestBody PostReportPayload postReportPayload) {
    PostReportDTO reportedPost = postReportService.reportPost(postReportPayload);
    return new ResponseEntity<>(reportedPost, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Report a comment")
  @ApiResponse(code = 201, message = "Comment reported")
  @PostMapping(value = "/comment")
  public ResponseEntity<CommentReportDTO> reportComment(@RequestBody CommentReportPayload commentReportPayload) {
    CommentReportDTO reportedComment = commentReportService.reportComment(commentReportPayload);
    return new ResponseEntity<>(reportedComment, HttpStatus.CREATED);
  }
}
