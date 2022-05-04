package com.writemaster.platform.service.impl;

import com.writemaster.platform.dto.PostReportDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Post;
import com.writemaster.platform.entity.PostReport;
import com.writemaster.platform.entity.PostReportPK;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.PostNotFoundException;
import com.writemaster.platform.mapper.PostReportMapper;
import com.writemaster.platform.payload.PostReportPayload;
import com.writemaster.platform.payload.ReviewPostReportPayload;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.PostReportRepository;
import com.writemaster.platform.repository.PostRepository;
import com.writemaster.platform.service.PostReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostReportServiceImpl implements PostReportService {
  private final PostReportRepository postReportRepository;
  private final AuthorRepository authorRepository;
  private final PostRepository postRepository;
  private final PostReportMapper postReportMapper;

  public PostReportServiceImpl(PostReportRepository postReportRepository, AuthorRepository authorRepository, PostRepository postRepository, PostReportMapper postReportMapper) {
    this.postReportRepository = postReportRepository;
    this.authorRepository = authorRepository;
    this.postRepository = postRepository;
    this.postReportMapper = postReportMapper;
  }

  @Override
  @Transactional
  public PostReportDTO reportPost(PostReportPayload postReportPayload) {
    Author reporter = authorRepository.findAuthorById(postReportPayload.getReporterId());
    if (Optional.ofNullable(reporter).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + postReportPayload.getReporterId());
    }
    Post post = postRepository.findPostById(postReportPayload.getPostId());
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postReportPayload.getPostId());
    }

    PostReport report = new PostReport();
    report.setPostReportPK(new PostReportPK(postReportPayload.getReporterId(), postReportPayload.getPostId()));
    report.setPost(post);
    report.setReporter(reporter);
    report.setReason(postReportPayload.getReason());
    report.setStateOfReviewal(postReportPayload.getStateOfReviewal());

    return postReportMapper.fromEntity(postReportRepository.save(report));
  }

  @Override
  @Transactional
  public PostReportDTO reviewPostReport(ReviewPostReportPayload reviewPostReportPayload) {
    PostReport report = postReportRepository.findByReporterIdAndPostId(reviewPostReportPayload.getReporterId(), reviewPostReportPayload.getPostId());

    report.setStateOfReviewal(reviewPostReportPayload.getStateOfReviewal());
    report.setReviewedOn(reviewPostReportPayload.getReviewedOn());

    return postReportMapper.fromEntity(postReportRepository.save(report));
  }

  @Override
  @Transactional
  public Set<PostReportDTO> getAllPostReports() {
    return postReportRepository.findAll().stream().map(postReportMapper::fromEntity).collect(Collectors.toSet());
  }
}
