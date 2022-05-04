package com.writemaster.platform.service;

import com.writemaster.platform.dto.PostDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Post;
import com.writemaster.platform.exception.NotFoundException;
import com.writemaster.platform.mapper.PostMapper;
import com.writemaster.platform.payload.CreatePostPayload;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.PostRepository;
import com.writemaster.platform.service.impl.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PostServiceImplTests {
  @Mock
  private PostRepository postRepository;

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private PostMapper postMapper;

  @Captor
  private ArgumentCaptor<Post> captor;

  @InjectMocks
  private PostServiceImpl underTest;

  @Test
  public void testFindPostByIdGivenIdNotExistInDatabaseShouldThrowNotFoundException() {
    NotFoundException exception = assertThrows(NotFoundException.class, () -> underTest.findPostById(1));
    assertEquals("No record found with id 1", exception.getMessage());
  }

  @Test
  public void testFindPostByIdGivenIdExistInDatabaseShouldReturnDataSuccessfully() {
    Author mockAuthor = mock(Author.class);
    when(mockAuthor.getId()).thenReturn(1);
    when(mockAuthor.getUsername()).thenReturn("tranduong");

    Post mockPost = mock(Post.class);
    when(mockPost.getId()).thenReturn(1);
    when(mockPost.getTitle()).thenReturn("Post title");
    when(mockPost.getContent()).thenReturn("Post content");
    when(mockPost.getAuthor()).thenReturn(mockAuthor);

    Mockito.when(authorRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockAuthor));
    Mockito.when(postRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockPost));
    when(postMapper.fromEntity(mockPost)).thenCallRealMethod();
    PostDTO post = underTest.findPostById(1);
    assertEquals(mockPost.getTitle(), post.getTitle());
    assertEquals(mockPost.getContent(), post.getContent());
  }

  @Test
  public void testDeletePostGivenIdNotFoundShouldThrowNotFoundException() {
    NotFoundException exception = assertThrows(NotFoundException.class, () -> underTest.deletePostById(1));
    assertEquals("No record found with id 1", exception.getMessage());
    verify(postRepository, never()).delete(any(Post.class));
  }

  @Test
  public void testDeletePostGivenIdExistShouldDeleteSuccessfully() {
    Post mockPost = mock(Post.class);
    when(mockPost.getId()).thenReturn(1);
    when(mockPost.getTitle()).thenReturn("Title mock post");
    when(postRepository.findById(anyInt())).thenReturn(Optional.of(mockPost));
    underTest.deletePostById(1);
    verify(postRepository, times(1)).delete(captor.capture());
    Post deletedPost = captor.getValue();
    assertEquals("Title mock post", deletedPost.getTitle());
    assertEquals(1, deletedPost.getId());
  }

  /**
   * do the same for
   * findPostByAuthorInMonth
   * findPostByCreatedDate
   * findTop10NewestPost
   * findByTitle
   */
  @Test
  public void testFindByTitleLikeEmptyResultShouldNeverCallMapper() {
    when(postRepository.findByTitleContains(anyString())).thenReturn(Collections.emptyList());
    underTest.findByTitleLike("mock post");
    verify(postMapper, never()).fromEntity(any(Post.class));
  }

  @Test
  public void testFindByTitleEqualEmptyResultShouldNeverCallMapper() {
    when(postRepository.findByTitle(anyString())).thenReturn(Collections.emptyList());
    underTest.findByTitle("mock post");
    verify(postMapper, never()).fromEntity(any(Post.class));
  }

  /**
   * do the same for
   * findPostByAuthorInMonth
   * findPostByCreatedDate
   * findTop10NewestPost
   * findByTitle
   */
  @Test
  public void testFindByTitleLikeReturn2PostsShouldCallMapper2Times() {
    Post first = new Post("Java 8 tutorial");
    Post second = new Post("Getting started with Javascript");
    List<Post> posts = java.util.Arrays.asList(first, second);
    when(postRepository.findByTitleContains(anyString())).thenReturn(posts);
    underTest.findByTitleLike("Java");
    verify(postMapper, times(2)).fromEntity(any()); // verify mapper call 2 times
    // because results return 2 elements
  }

  @Test
  public void testFindTop10NewestPostsShouldCallMapper10Times() {
    Post post = new Post();
    List<Post> posts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      posts.add(post);
    }
    when(postRepository.findTop10ByOrderByCreatedDateDesc()).thenReturn(posts);
    underTest.findTop10NewestPost();
    verify(postMapper, times(10)).fromEntity(any(Post.class));
  }

  @Nested
  @DisplayName("Test Update Post")
  class TestUpdatePost {
    @Test
    public void testUpdatePostGivenPostIdIsNullShouldThrowException() {
      PostDTO mockPayload = mock(PostDTO.class);
      NotFoundException exception = assertThrows(NotFoundException.class, () -> underTest.updatePost(null, mockPayload));
      assertEquals("No author of username: null", exception.getMessage());
      verify(postRepository, never()).findById(anyInt());
    }

    @Test
    public void testUpdatePostGivenPayloadIsNullShouldThrowException() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.updatePost(1, null));
      assertEquals("Request payload can not be null", exception.getMessage());
      verify(postRepository, never()).findById(anyInt());
    }

    @Test
    public void testUpdatePostGivenIdAndPayloadNotNullButNotFoundPostInDbShouldThrowNotFoundException() {
      PostDTO mockPayload = mock(PostDTO.class);
      when(mockPayload.getTitle()).thenReturn("Updated title");
      when(mockPayload.getContent()).thenReturn("Updated content");
      when(mockPayload.getDescription()).thenReturn("New desc");
      when(postRepository.findById(anyInt())).thenReturn(Optional.empty());
      assertThrows(NotFoundException.class, () -> underTest.updatePost(1, mockPayload));
      verify(postMapper, never()).fromEntity(any(Post.class));
      verify(postRepository, never()).save(any(Post.class));
      verify(postMapper, never()).fromDTO(any(PostDTO.class));
    }

    @Test
    public void testUpdatePostGivenInvalidPayloadShouldThrowNotFoundException() {
      Post mockPost = mock(Post.class);
      when(mockPost.getId()).thenReturn(1);
      when(mockPost.getTitle()).thenReturn("Old title");
      when(mockPost.getContent()).thenReturn("Old content");
      when(mockPost.getDescription()).thenReturn("Old desc");

      PostDTO mockPayload = mock(PostDTO.class);
      when(mockPayload.getTitle()).thenReturn("New title");
      when(mockPayload.getContent()).thenReturn("New content");
      when(mockPayload.getDescription()).thenReturn("New desc");

      when(postRepository.findById(anyInt())).thenReturn(Optional.of(mockPost));
      when(postMapper.fromDTO(any(Post.class), any(PostDTO.class))).thenCallRealMethod();

      assertThrows(NotFoundException.class, () -> underTest.updatePost(1, mockPayload));
      verify(postMapper, never()).fromDTO(any(Post.class), any(PostDTO.class));
      verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    public void testUpdatePostGivenValidPayloadShouldUpdatePostSuccessfully() {
      Author mockAuthor = mock(Author.class);
      when(mockAuthor.getId()).thenReturn(1);
      when(mockAuthor.getUsername()).thenReturn("tranduong");

      Post mockPost = mock(Post.class);
      when(mockPost.getTitle()).thenReturn("Old title");
      when(mockPost.getDescription()).thenReturn("Old description");
      when(mockPost.getContent()).thenReturn("Old content");
      when(mockPost.getAuthor()).thenReturn(mockAuthor);

      PostDTO mockPayload = mock(PostDTO.class);
      when(mockPayload.getTitle()).thenReturn("New title");
      when(mockPayload.getDescription()).thenReturn("New description");
      when(mockPayload.getContent()).thenReturn("New content");
      when(mockPayload.getAuthor()).thenReturn("tranduong");
      when(mockPayload.getAuthorId()).thenReturn(1);

      when(authorRepository.findByUsername("tranduong")).thenReturn(mockAuthor);
      when(authorRepository.findById(1)).thenReturn(Optional.of(mockAuthor));
      when(postRepository.findById(anyInt())).thenReturn(Optional.of(mockPost));
      when(postMapper.fromDTO(any(Post.class), any(PostDTO.class))).thenCallRealMethod();

      assertDoesNotThrow(() -> underTest.updatePost(1, mockPayload));
      verify(authorRepository, times(1)).findByUsername(anyString());
      verify(authorRepository, times(1)).findById(anyInt());
      verify(postRepository, times(1)).findById(anyInt());
      verify(postMapper, times(1)).fromDTO(any(Post.class), any(PostDTO.class));
    }
  }

  @Nested
  @DisplayName("Test Create Post")
  class TestCreatePost {
    @Test
    public void testCreatePostGivenAuthorIdIsNullShouldThrowException() {
      CreatePostPayload mockPayload = mock(CreatePostPayload.class);
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.createPost(mockPayload));
      assertEquals("Author id can not be null", exception.getMessage());
      verify(postRepository, never()).findById(anyInt());
    }

    @Test
    public void testCreatePostGivenPayloadIsNullShouldThrowException() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.createPost(null));
      assertEquals("Request payload can not be null", exception.getMessage());
      verify(postRepository, never()).findById(anyInt());
    }

    @Test
    public void testCreatePostSuccess() {
      Author mockAuthor = mock(Author.class);
      when(mockAuthor.getId()).thenReturn(1);
      when(authorRepository.findById(anyInt())).thenReturn(Optional.of(mockAuthor));

      CreatePostPayload mockPayload = mock(CreatePostPayload.class);
      when(mockPayload.getTitle()).thenReturn("New post title");
      when(mockPayload.getContent()).thenReturn("New post content");
      when(mockPayload.getDescription()).thenReturn("New post desc");

      when(postMapper.fromDTO(any(PostDTO.class))).thenCallRealMethod();
      when(postMapper.fromDTO(any(Post.class), any(PostDTO.class))).thenCallRealMethod();

      assertDoesNotThrow(() -> underTest.createPost(mockPayload));

      verify(postMapper, times(1)).fromDTO(any(Post.class), any(PostDTO.class));

      verify(postRepository, times(1)).save(any(Post.class));
    }
  }
}
