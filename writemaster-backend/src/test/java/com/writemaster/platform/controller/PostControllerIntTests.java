package com.writemaster.platform.controller;

import com.writemaster.platform.exception.NotFoundException;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/add-author.sql", "/add-post.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"/remove-post.sql", "/remove-author.sql"})
public class PostControllerIntTests {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName(value = "Test Post controller with correct credentials")
  @Nested
  @WithMockUser(username = "tranduong", password = "1234", roles = {"AUTHOR"})
  public class TestPostControllerWithCorrectCredentials {
    @Test
    public void getAllPosts() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/posts")
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$").isArray())
              .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(14)))
              .andExpect(jsonPath("$[*].id").isNotEmpty());
    }

    @ParameterizedTest(name = "Test: Get post with id {0} not found")
    @ValueSource(ints = {200, -1, 15})
    public void getByIdNotFound(int id) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/posts/id/" + id)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundException))
              .andExpect(result -> Assertions.assertEquals("No record found with id " + id, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @ParameterizedTest(name = "Test: Get post with id {0} found")
    @ValueSource(ints = {1, 2, 3})
    public void getByIdFound(int id) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/posts/id/" + id)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(id))
              .andExpect(jsonPath("$.title").value("Post " + id));
    }

    @ParameterizedTest(name = "Test: Get posts by valid author should return correct number of posts")
    @ValueSource(strings = {"tranduong", "JamesGosling"})
    public void getPostsByValidAuthor(String author) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/posts/author/" + author)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$").isArray())
              .andExpect(jsonPath("$[*].id").isNotEmpty())
              .andExpect(jsonPath("$[*].title").isNotEmpty())
              .andExpect(jsonPath("$[*].description").isNotEmpty())
              .andExpect(jsonPath("$[*].content").isNotEmpty());
    }

    @ParameterizedTest(name = "Test: Get posts by invalid author should return correct number of posts")
    @ValueSource(strings = {"Alien", "Predator"})
    public void getPostsByInvalidAuthor(String author) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/posts/author/" + author)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundException))
              .andExpect(result -> Assertions.assertEquals("No author with username " + author, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void get10LatestPosts() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/posts/top10")
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$").isArray())
              .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(10)))
              .andExpect(jsonPath("$[*].id").isNotEmpty())
              .andExpect(jsonPath("$[*].title").isNotEmpty())
              .andExpect(jsonPath("$[*].description").isNotEmpty())
              .andExpect(jsonPath("$[*].content").isNotEmpty());
    }

    @DisplayName(value = "Test: Delete post by id")
    @Nested
    public class TestDeletePostById {

      @ParameterizedTest(name = "Test: Delete post by correct id should delete post properly")
      @ValueSource(ints = {10, 11})
      public void testDeletePostGivenCorrectIdShouldDeletePost(int id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/" + id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
      }

      @ParameterizedTest(name = "Test: Delete post by invalid id should throw NotFoundException")
      @ValueSource(ints = {1000, -40, 20, 458})
      public void testDeletePostGivenInvalidIdShouldThrowNotFoundException(int id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/" + id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> Assertions.assertEquals("No record found with id " + id, Objects.requireNonNull(result.getResolvedException()).getMessage()));
      }
    }

  }
}
