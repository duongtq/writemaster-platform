package com.writemaster.platform.service.impl;

import com.writemaster.platform.dto.PostDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Post;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.InvalidDataFormatException;
import com.writemaster.platform.exception.PostNotFoundException;
import com.writemaster.platform.mapper.PostMapper;
import com.writemaster.platform.payload.CreatePostPayload;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.PostRepository;
import com.writemaster.platform.service.PostService;
import com.writemaster.platform.utils.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
  private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
  private final PostRepository postRepository;
  private final AuthorRepository authorRepository;
  private final PostMapper postMapper;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PostServiceImpl(PostRepository postRepository, AuthorRepository authorRepository, PostMapper mapper, JdbcTemplate jdbcTemplate) {
    this.postRepository = postRepository;
    this.authorRepository = authorRepository;
    this.postMapper = mapper;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<PostDTO> getAllPostList() {
    logger.info("Get all posts: START");
    List<Post> posts = postRepository.findAll();
    logger.info("Get all posts: DONE");
    return posts.stream().filter(post -> Optional.ofNullable(post.getAuthor()).isPresent()).map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  public PostDTO findPostById(Integer id) {
    logger.info("Find post by id {}: START", id);
    Post post = postRepository.findById(id).orElse(null);
    if (Optional.ofNullable(post).isEmpty()) {
      logger.error("Find post by id {}: FAILED - No post of id {}", id, id);
      throw new PostNotFoundException("No record found with id " + id);
    }
    logger.info("Find post by {}: DONE", id);
    return postMapper.fromEntity(post);
  }

  @Override
  @Transactional
  public void deletePostById(Integer id) {
    logger.info("Delete post by id {}: START", id);
    Post post = postRepository.findPostById(id);
    if (Optional.ofNullable(post).isEmpty()) {
      logger.error("Delete post by id {}: FAILED - No post of id {}", id, id);
      throw new PostNotFoundException("No record found with id " + id);
    }
    String deleteLikes = "DELETE FROM likes WHERE post_id = ?";
    String deleteComments = "DELETE FROM comments WHERE post_id = ?";
    String deleteBookmarks = "DELETE FROM bookmarks WHERE post_id = ?";
    String deletePost = "DELETE FROM posts WHERE id = ?";

    jdbcTemplate.update(deleteLikes, id);
    jdbcTemplate.update(deleteComments, id);
    jdbcTemplate.update(deleteBookmarks, id);
    jdbcTemplate.update(deletePost, id);

    logger.info("Delete post by id {}: DONE", id);
  }

  @Override
  public List<PostDTO> findByTitleLike(String keyword) {
    logger.info("Find posts by title like {}: START", keyword);
    List<Post> posts = postRepository.findByTitleContains(keyword);
    logger.info("Find posts by title like {}: DONE", keyword);
    return posts.stream().map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  public List<PostDTO> findByTitle(String title) {
    logger.info("Find posts by title equal {}: START", title);
    List<Post> posts = postRepository.findByTitle(title);
    logger.info("Find posts by title equal {}: DONE", title);
    return posts.stream().map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<PostDTO> findTop10NewestPost() {
    logger.info("Find top 10 latest posts: START");
    List<Post> posts = postRepository.findTop10ByOrderByCreatedDateDesc();
    logger.info("Find top 10 latest posts: DONE");
    return posts.stream().map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<PostDTO> findPostByCreatedDate(LocalDate date) {
    logger.info("Find posts created in {}: START", date.toString());
    Instant fromDate = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    Instant toDate = date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
    List<Post> posts = postRepository
            .findByDateBetween(Date.from(fromDate), Date.from(toDate));
    logger.info("Find posts created in {}: DONE", date);
    return posts.stream().map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<PostDTO> findPostByAuthorInMonth(Integer authorId, int month) {
    logger.warn("Find all posts of author id {} in month {}: START", authorId, month);
    if (Optional.ofNullable(authorId).isEmpty()) {
      logger.error("Find all posts of author id {} month {}: FAILED - Null author id", authorId, month);
      throw new IllegalArgumentException("Author id can not be null");
    }
    if (month < 1 || month > 12) {
      logger.error("Find all posts of author id {} month {}: FAILED - Invalid month value {}", authorId, month, month);
      throw new IllegalArgumentException("Invalid month value: " + month);
    }

    Optional<Author> author = authorRepository.findById(authorId);
    if (author.isEmpty()) {
      logger.error("Find all posts of author id {} month {}: FAILED - No author of id {}", authorId, month, authorId);
      throw new IllegalArgumentException("No author with id: " + authorId);
    }

    LocalDateTime start = LocalDate.now().withMonth(month).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    Instant fromDate = start.atZone(ZoneId.systemDefault()).toInstant();

    LocalDateTime end = LocalDate.now().withMonth(month).with(TemporalAdjusters.lastDayOfMonth())
            .atTime(LocalTime.MAX);
    Instant toDate = end.atZone(ZoneId.systemDefault()).toInstant();
    List<Post> posts = postRepository.findByAuthorIdAndCreatedDateBetween(
            authorId, Date.from(fromDate), Date.from(toDate));
    logger.error("Find all posts of author id {} month {}: DONE", authorId, month);
    return posts.stream().map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public PostDTO updatePost(Integer id, PostDTO payload) {
    logger.info("Update post id {}: START", id);

    if (Optional.ofNullable(payload).isEmpty()) {
      logger.error("Update post id {}: FAILED - Null request payload", id);
      throw new IllegalArgumentException("Request payload can not be null");
    }

    Author author1 = authorRepository.findByUsername(payload.getAuthor());
    if (Optional.ofNullable(author1).isEmpty()) {
      logger.error("Update post id {}: FAILED - No author of username {}", id, payload.getAuthor());
      throw new AuthorNotFoundException("No author of username: " + payload.getAuthor());
    } else {
      if (author1.getId() != payload.getAuthorId()) {
        logger.error("Update post id {}: FAILED - Username and ID not match", id);
        throw new IllegalArgumentException("Username and ID not match");
      }
    }

    Optional<Author> author = authorRepository.findById(payload.getAuthorId());
    if (author.isEmpty()) {
      logger.error("Update post id {}: FAILED - No author of id {}", id, payload.getAuthorId());
      throw new AuthorNotFoundException("No author of id: " + payload.getAuthorId());
    }

    if (Optional.ofNullable(id).isEmpty()) {
      logger.error("Update post id {}: FAILED - Null post id", id);
      throw new IllegalArgumentException("Post id can not be null");
    }

    Post post = postRepository.findById(id).orElse(new Post());

    if (Optional.ofNullable(post.getId()).isEmpty()) {
      logger.error("Update post id {}: FAILED - No post of id {}", id, id);
      throw new PostNotFoundException("Post " + id + " not found");
    }

    if (DataValidator.validatePostDTO(payload)) {
      logger.error("Update post id {}: FAILED - Payload invalid", id);
      throw new InvalidDataFormatException("Data can not be null or empty");
    }

    Post postToUpdate = postMapper.fromDTO(post, payload);
    Post updatedPost = postRepository.save(postToUpdate);
    logger.info("Update post id {}: DONE", id);
    return postMapper.fromEntity(updatedPost);
  }

  @Override
  @Transactional
  public PostDTO createPost(CreatePostPayload createPostPayload) {
    Integer authorId = createPostPayload.getAuthorId();
    logger.info("Create post of author id {}: START", authorId);

    if (Optional.ofNullable(authorId).isEmpty()) {
      logger.error("Create post of author id {}: FAILED - Null author id", authorId);
      throw new IllegalArgumentException("Author id can not be null");
    }

    Author author = authorRepository.findById(authorId).orElse(null);
    if (Optional.ofNullable(author).isEmpty()) {
      logger.error("Create post of author id {}: FAILED - No author of id {}", authorId, authorId);
      throw new AuthorNotFoundException("No author with id: " + authorId);
    }

    Post post = new Post();
    post.setTitle(createPostPayload.getTitle());
    post.setDescription(createPostPayload.getDescription());
    post.setContent(createPostPayload.getContent());
    post.setCreatedDate(createPostPayload.getCreatedDate());
    post.setAuthor(author);
    post.setNumberOfLikes(0);
    post.setNumberOfComments(0);
    post.setNumberOfBookmarks(0);
    logger.info("Create post of author id {}: DONE", authorId);
    return postMapper.fromNewEntity(postRepository.save(post));
  }

  @Override
  @Transactional
  public List<PostDTO> findPostsByAuthor(String username) {
    logger.info("Find posts of author {}: START", username);
    boolean isAuthorExistent = authorRepository.existsByUsername(username);
    if (!isAuthorExistent) {
      logger.error("Find posts of author {}: FAILED - No author of username {}", username, username);
      throw new AuthorNotFoundException("No author with username " + username);
    }
    Author author = authorRepository.findByUsername(username);
    List<Post> posts = postRepository.findAllByAuthor(author);
    logger.info("Find posts of author {}: DONE", username);
    return posts.stream().map(postMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Set<PostDTO> findBookmarkedPostsByAuthorId(Integer authorId) {
    logger.info("Find bookmarked posts of author id {}", authorId);
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      logger.error("Find posts of author {}: FAILED - No author of id {}", authorId, authorId);
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    Set<Post> bookmarkedPosts = postRepository.findBookmarkedPostsByAuthorId(authorId);
    return bookmarkedPosts.stream().map(postMapper::fromEntity).collect(Collectors.toSet());
  }
}
