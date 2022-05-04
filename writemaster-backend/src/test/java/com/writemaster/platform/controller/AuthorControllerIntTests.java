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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/add-author.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/remove-author.sql")
public class AuthorControllerIntTests {
  @Autowired
  MockMvc mockMvc;

  @DisplayName(value = "Test Author controller should work properly with correct credentials")
  @Nested
  @WithMockUser(username = "tranduong", password = "1234", roles = {"ADMIN"})
  public class TestAuthorControllerWithCorrectCredentials {
    @Test
    public void getAllAuthors() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/"))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$").isArray())
              .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(4)))
              .andExpect(jsonPath("$[*].id").isNotEmpty());
    }

    @ParameterizedTest(name = "Test: Get author by existent id [{0}] should return correct author")
    @ValueSource(ints = {1, 2, 3, 4})
    public void testGetAuthorByIdGivenExistentIdShouldReturnCorrectAuthor(int id) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/id/" + id)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").isNotEmpty())
              .andExpect(jsonPath("$.username").isNotEmpty())
              .andExpect(jsonPath("$.password").isNotEmpty())
              .andExpect(jsonPath("$.firstName").isNotEmpty())
              .andExpect(jsonPath("$.lastName").isNotEmpty())
              .andExpect(jsonPath("$.id").value(id));
    }

    @ParameterizedTest(name = "Test: Get author by non-existent id [{0}] should throw exception")
    @ValueSource(ints = {200, 1000, -8, 0})
    public void testGetAuthorByIdGivenNonExistentIdShouldThrowException(int id) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/id/" + id)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundException))
              .andExpect(result -> Assertions.assertEquals("No author with id: " + id, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @ParameterizedTest(name = "Test: Get author by existent username [{0}] should return correct author")
    @ValueSource(strings = {"tranduong", "MinhNghien"})
    public void testGetAuthorByUsernameGivenExistentUsernameShouldReturnCorrectAuthor(String username) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/username/" + username)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").isNotEmpty())
              .andExpect(jsonPath("$.username").isNotEmpty())
              .andExpect(jsonPath("$.password").isNotEmpty())
              .andExpect(jsonPath("$.firstName").isNotEmpty())
              .andExpect(jsonPath("$.lastName").isNotEmpty())
              .andExpect(jsonPath("$.email").isNotEmpty())
              .andExpect(jsonPath("$.birthDate").isNotEmpty())
              .andExpect(jsonPath("$.createdDate").isNotEmpty())
              .andExpect(jsonPath("$.username").value(username));
    }

    @ParameterizedTest(name = "Test: Get author by existent username [{0}] should return correct author")
    @ValueSource(strings = {"Alien", "Predator"})
    public void testGetAuthorByUsernameGivenNonExistentUsernameShouldThrowException(String username) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/username/" + username)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundException))
              .andExpect(result -> Assertions.assertEquals("No author with username: " + username, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
  }

  @DisplayName(value = "Test Author controller should always return 401 with invalid credentials")
  @Nested
  public class TestAuthorControllerWithInCorrectCredentials {
    @Test
    public void getAllAuthors() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/"))
              .andDo(print())
              .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "Test: Get author by existent id [{0}] should return correct author")
    @ValueSource(ints = {1, 2, 3, 4})
    public void testGetAuthorByIdGivenExistentIdShouldReturnCorrectAuthor(int id) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/id/" + id)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "Test: Get author by non-existent id [{0}] should throw exception")
    @ValueSource(ints = {200, 1000, -8, 0})
    public void testGetAuthorByIdGivenNonExistentIdShouldThrowException(int id) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/id/" + id)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "Test: Get author by existent username [{0}] should return correct author")
    @ValueSource(strings = {"tranduong", "MinhNghien"})
    public void testGetAuthorByUsernameGivenExistentUsernameShouldReturnCorrectAuthor(String username) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/username/" + username)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "Test: Get author by existent username [{0}] should return correct author")
    @ValueSource(strings = {"Alien", "Predator"})
    public void testGetAuthorByUsernameGivenNonExistentUsernameShouldThrowException(String username) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
                      .get("/api/v1/admin/authors/username/" + username)
                      .accept(MediaType.APPLICATION_JSON_VALUE))
              .andDo(print())
              .andExpect(status().isUnauthorized());
    }
  }
}
