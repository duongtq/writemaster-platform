package com.writemaster.platform.repository;

import com.writemaster.platform.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/add-author.sql", "/add-post.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"/remove-post.sql", "/remove-author.sql"})
public class PostRepositoryTests {
  @Autowired
  private PostRepository postRepository;

  @Test
  public void testGetAllPosts() {
    List<Post> posts = postRepository.findAll();
    assertEquals(14, posts.size());
  }

  @DisplayName(value = "Test: Find posts by title")
  @Nested
  public class TestFindPostByTitle {
    @Test
    public void testFindByTitleLikeGivenCorrectTitleShouldReturnRecordsCorrectly() {
      List<Post> posts = postRepository.findByTitleContains("Post 1");
      assertEquals(6, posts.size());
      for (Post post : posts) {
        assertTrue(post.getTitle().contains("Post 1"));
      }
    }

    @Test
    public void testFindByTitleLikeGivenIncorrectTitleShouldReturnEmptyRecordList() {
      List<Post> posts = postRepository.findByTitleContains("Predator");
      assertEquals(0, posts.size());
    }

    @ParameterizedTest(name = "Test find post by title equal [{0}] should return correct post")
    @ValueSource(strings = {"Post 1", "Post 2", "Post 3", "Post 4"})
    public void testFindByTitleEqualGivenCorrectTitleShouldReturnRecordsCorrectly(String title) {
      List<Post> posts = postRepository.findByTitle(title);
      assertEquals(1, posts.size());
      for (Post post : posts) {
        assertEquals(post.getTitle(), title);
      }
    }

    @ParameterizedTest(name = "Test find post by title equal [{0}] should return empty record list")
    @ValueSource(strings = {"Post 111", "Post 222", "Post 333", "Post 444"})
    public void testFindByTitleEqualGivenIncorrectTitleShouldReturnEmptyRecordList(String title) {
      List<Post> posts = postRepository.findByTitle(title);
      assertEquals(0, posts.size());
    }
  }
}
