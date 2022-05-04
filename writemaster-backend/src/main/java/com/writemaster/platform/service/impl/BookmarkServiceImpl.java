package com.writemaster.platform.service.impl;

import com.writemaster.platform.common.BookmarkState;
import com.writemaster.platform.dto.BookmarkDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Bookmark;
import com.writemaster.platform.entity.Post;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.BookmarkNotFoundException;
import com.writemaster.platform.exception.PostNotFoundException;
import com.writemaster.platform.mapper.BookmarkMapper;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.BookmarkRepository;
import com.writemaster.platform.repository.PostRepository;
import com.writemaster.platform.service.BookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {
  private final Logger logger = LoggerFactory.getLogger(BookmarkServiceImpl.class);

  private final BookmarkRepository bookmarkRepository;
  private final AuthorRepository authorRepository;
  private final PostRepository postRepository;
  private final BookmarkMapper bookmarkMapper;
  private final JdbcTemplate jdbcTemplate;

  public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, AuthorRepository authorRepository,
                             PostRepository postRepository, BookmarkMapper bookmarkMapper, JdbcTemplate jdbcTemplate) {
    this.bookmarkRepository = bookmarkRepository;
    this.authorRepository = authorRepository;
    this.postRepository = postRepository;
    this.bookmarkMapper = bookmarkMapper;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  @Transactional
  public BookmarkDTO bookmarkPost(Integer authorId, Integer postId) {
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }

    post.setNumberOfBookmarks(post.getNumberOfBookmarks() + 1);
    postRepository.save(post);

    Bookmark newBookmark = new Bookmark();
    newBookmark.setBookmarkedOn(new Date());
    newBookmark.setAuthor(author);
    newBookmark.setPost(post);

    return bookmarkMapper.fromEntity(bookmarkRepository.save(newBookmark), BookmarkState.CREATED);
  }

  @Override
  @Transactional
  public BookmarkDTO unbookmarkPost(Integer authorId, Integer postId, Date sharedOn) {
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }

    Bookmark shared = bookmarkRepository.findBookmarkByAuthorIdAndPostIdAndBookmarkedOn(authorId, postId, sharedOn);
    if (Optional.ofNullable(shared).isEmpty()) {
      throw new BookmarkNotFoundException(String.format("No share of author id %d on post id %d", authorId, postId));
    }

    post.setNumberOfBookmarks(post.getNumberOfBookmarks() - 1);
    postRepository.save(post);
    bookmarkRepository.delete(shared);
    return bookmarkMapper.fromEntity(shared, BookmarkState.DELETED);
  }

  @Override
  @Transactional
  public List<BookmarkDTO> getAllBookmarkedPostsByAuthorId(Integer authorId) {
    return bookmarkRepository.getAllByAuthorIdOrderByBookmarkedOnAsc(authorId)
            .stream()
            .map(bookmarkMapper::fromEntity)
            .collect(Collectors.toList());
  }

  @Override
  public BookmarkDTO unbookmarkPost(Integer authorId, Integer postId) {
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }

    Bookmark shared = bookmarkRepository.findBookmarkByAuthorIdAndPostId(authorId, postId);
    if (Optional.ofNullable(shared).isEmpty()) {
      throw new BookmarkNotFoundException(String.format("No share of author id %d on post id %d", authorId, postId));
    }

    post.setNumberOfBookmarks(post.getNumberOfBookmarks() - 1);
    postRepository.save(post);

    // FIXME: be careful here
    String deleteBookmarkByAuthorIdAndPostId = "DELETE FROM bookmarks WHERE author_id = ? AND post_id = ?";

    jdbcTemplate.update(deleteBookmarkByAuthorIdAndPostId, authorId, postId);

    logger.warn("Bookmark deleted: author id {} - post id: {}", authorId, postId);
    return bookmarkMapper.fromEntity(shared, BookmarkState.DELETED);
  }
}
