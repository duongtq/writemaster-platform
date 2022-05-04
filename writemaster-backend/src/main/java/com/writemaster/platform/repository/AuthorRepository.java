package com.writemaster.platform.repository;

import com.writemaster.platform.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
  Author findByUsername(String username);

  boolean existsByUsername(String username);

  Author findAuthorByEmail(String email);

  Author findAuthorById(Integer authorId);
}
