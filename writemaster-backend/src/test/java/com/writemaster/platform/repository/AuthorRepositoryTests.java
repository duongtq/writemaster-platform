package com.writemaster.platform.repository;


import com.writemaster.platform.entity.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/add-author.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/remove-author.sql")
public class AuthorRepositoryTests {
  @Autowired
  AuthorRepository authorRepository;

  @Test
  public void testGetAllAuthors() {
    List<Author> authors = authorRepository.findAll();
    assertEquals(4, authors.size());
  }

  @ParameterizedTest(name = "Test: Get author by id [{0}] should return correct author")
  @ValueSource(ints = {1, 2, 3, 4})
  public void testGetAuthorById(int id) {
    Optional<Author> author = authorRepository.findById(id);
    assertTrue(author.isPresent());
    assertEquals(id, author.get().getId());
  }

  @ParameterizedTest(name = "Test: Get author by non-existent id [{0}] should return null")
  @ValueSource(ints = {5, 6})
  public void testGetAuthorByNonExistentId(int id) {
    Optional<Author> author = authorRepository.findById(id);
    assertTrue(author.isEmpty());
  }

  @ParameterizedTest(name = "Test: Check existence of an author with username [{0}] should return correct author")
  @ValueSource(strings = {"JamesGosling", "muvodoi"})
  public void testGetAuthorByUsername(String username) {
    boolean existence = authorRepository.existsByUsername(username);
    assertTrue(existence);
  }

  @ParameterizedTest(name = "Test: Check existence of an author with non-existent username [{0}] should return null")
  @ValueSource(strings = {"RobertDowney", "Alien"})
  public void testGetAuthorByNonExistentUsername(String username) {
    Author author = authorRepository.findByUsername(username);
    assertTrue(Optional.ofNullable(author).isEmpty());
  }
}
