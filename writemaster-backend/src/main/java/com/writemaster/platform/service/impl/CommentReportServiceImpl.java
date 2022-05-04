package com.writemaster.platform.service.impl;

import com.writemaster.platform.dto.CommentReportDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Comment;
import com.writemaster.platform.entity.CommentPKReport;
import com.writemaster.platform.entity.CommentReport;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.CommentNotFoundException;
import com.writemaster.platform.mapper.CommentReportMapper;
import com.writemaster.platform.payload.CommentReportPayload;
import com.writemaster.platform.payload.ReviewCommentReportPayload;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.CommentReportRepository;
import com.writemaster.platform.repository.CommentRepository;
import com.writemaster.platform.service.CommentReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentReportServiceImpl implements CommentReportService {
  private final CommentReportRepository commentReportRepository;
  private final AuthorRepository authorRepository;
  private final CommentRepository commentRepository;
  private final CommentReportMapper commentReportMapper;

  public CommentReportServiceImpl(CommentReportRepository commentReportRepository, AuthorRepository authorRepository,
                                  CommentRepository commentRepository, CommentReportMapper commentReportMapper) {
    this.commentReportRepository = commentReportRepository;
    this.authorRepository = authorRepository;
    this.commentRepository = commentRepository;
    this.commentReportMapper = commentReportMapper;
  }


  @Override
  @Transactional
  public CommentReportDTO reportComment(CommentReportPayload commentReportPayload) {
    Author reporter = authorRepository.findAuthorById(commentReportPayload.getReporterId());
    if (Optional.ofNullable(reporter).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + commentReportPayload.getReporterId());
    }
    Comment comment = commentRepository.findCommentById(commentReportPayload.getCommentId());
    if (Optional.ofNullable(comment).isEmpty()) {
      throw new CommentNotFoundException("No comment of id " + commentReportPayload.getCommentId());
    }

    CommentReport commentReport = new CommentReport();
    commentReport.setCommentPKReport(new CommentPKReport(commentReportPayload.getReporterId(), commentReportPayload.getCommentId()));
    commentReport.setComment(comment);
    commentReport.setReporter(reporter);
    commentReport.setReason(commentReportPayload.getReason());
    commentReport.setStateOfReviewal(commentReportPayload.getStateOfReviewal());

    return commentReportMapper.fromEntity(commentReportRepository.save(commentReport));
  }

  @Override
  @Transactional
  public CommentReportDTO reviewCommentReport(ReviewCommentReportPayload reviewCommentReportPayload) {
    CommentReport commentReport = commentReportRepository.findByReporterIdAndCommentId(reviewCommentReportPayload.getReporterId(), reviewCommentReportPayload.getCommentId());
    commentReport.setStateOfReviewal(reviewCommentReportPayload.getStateOfReviewal());
    commentReport.setReviewedOn(reviewCommentReportPayload.getReviewedOn());
    return commentReportMapper.fromEntity(commentReportRepository.save(commentReport));
  }

  @Override
  @Transactional
  public Set<CommentReportDTO> getAllCommentReports() {
    return commentReportRepository.findAll().stream().map(commentReportMapper::fromEntity).collect(Collectors.toSet());
  }
}
