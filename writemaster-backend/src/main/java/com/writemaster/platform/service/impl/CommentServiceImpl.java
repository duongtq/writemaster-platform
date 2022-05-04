package com.writemaster.platform.service.impl;

import com.writemaster.platform.dto.CommentDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Comment;
import com.writemaster.platform.entity.Post;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.CommentNotFoundException;
import com.writemaster.platform.exception.PostNotFoundException;
import com.writemaster.platform.mapper.CommentMapper;
import com.writemaster.platform.payload.CreateCommentPayload;
import com.writemaster.platform.payload.DeleteCommentPayload;
import com.writemaster.platform.payload.UpdateCommentPayload;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.CommentRepository;
import com.writemaster.platform.repository.PostRepository;
import com.writemaster.platform.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
  private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final AuthorRepository authorRepository;
  private final CommentMapper commentMapper;

  public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, AuthorRepository authorRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.authorRepository = authorRepository;
    this.commentMapper = commentMapper;
  }

  @Override
  @Transactional
  public Set<CommentDTO> findCommentsByAuthorId(Integer authorId) {
    return commentRepository.findCommentsByAuthorId(authorId).stream().map(commentMapper::fromEntity).collect(Collectors.toSet());
  }

  @Override
  @Transactional
  public Set<CommentDTO> findCommentsByPostId(Integer postId) {
    return commentRepository.findCommentsByPostId(postId)
            .stream()
            .map(commentMapper::fromEntity)
            .collect(Collectors.toSet());
  }

  @Override
  @Transactional
  public CommentDTO addComment(Integer authorId, Integer postId, CreateCommentPayload createCommentPayload) {
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }

    post.setNumberOfComments(post.getNumberOfComments() + 1);
    postRepository.save(post);

    Comment comment = new Comment();
    comment.setAuthor(author);
    comment.setPost(post);
    comment.setContent(createCommentPayload.getContent());
    comment.setCreatedOn(createCommentPayload.getCreatedOn());

    Comment newComment = commentRepository.save(comment);
    logger.warn("New comment PK: {}", newComment.getId());

    return commentMapper.fromEntity(newComment);
  }

  @Override
  @Transactional
  public CommentDTO updateComment(Integer authorId, Integer postId, UpdateCommentPayload updateCommentPayload) {
    Comment comment = commentRepository.findCommentById(updateCommentPayload.getCommentId());
    if (Optional.ofNullable(comment).isEmpty()) {
    	throw new CommentNotFoundException("No comment of id " + updateCommentPayload.getCommentId());
    }

    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }

    if (!author.getUsername().equals(comment.getAuthor().getUsername()) ||
        !(Objects.equals(post.getId(), comment.getPost().getId()))) {
      throw new CommentNotFoundException("No comment found");
    }

    logger.warn("createdOn: {}", comment.getCreatedOn().toString());

    comment.setContent(updateCommentPayload.getContent());
    comment.setLastEditedOn(updateCommentPayload.getLastEditedOn());

    Comment updatedComment = commentRepository.save(comment);
    return commentMapper.fromEntity(updatedComment);
  }

  @Override
  @Transactional
  public void deleteComment(Integer authorId, Integer postId, DeleteCommentPayload deleteCommentPayload) {
    Comment comment = commentRepository.findCommentById(deleteCommentPayload.getCommentId());
    if (Optional.ofNullable(comment).isEmpty()) {
    	throw new CommentNotFoundException("No comment of id " + deleteCommentPayload.getCommentId());
    }
    
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }

    if (!author.getUsername().equals(comment.getAuthor().getUsername()) ||
            !(Objects.equals(post.getId(), comment.getPost().getId()))) {
      throw new CommentNotFoundException(String.format("No comment of author id %d on post id %d", authorId, postId));
    }

    post.setNumberOfComments(post.getNumberOfComments() - 1);
    postRepository.save(post);

    commentRepository.deleteCommentByIdAndAuthorIdAndPostId(deleteCommentPayload.getCommentId(), authorId, postId);
  }
}
