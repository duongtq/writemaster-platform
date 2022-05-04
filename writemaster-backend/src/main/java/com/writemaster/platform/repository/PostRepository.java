package com.writemaster.platform.repository;

import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
  List<Post> findByTitleContains(String keyword);

  List<Post> findByTitle(String title);

  @Query(value = "FROM Post p WHERE p.createdDate >= :from AND p.createdDate <= :to")
  List<Post> findByDateBetween(Date from, Date to);

  List<Post> findByAuthorIdAndCreatedDateBetween(Integer authorId, Date from, Date to);

  List<Post> findTop10ByOrderByCreatedDateDesc();

  List<Post> findAllByAuthor(Author author);

  Post findPostById(Integer postId);

  @Query(value = "FROM Post p WHERE p.id IN (SELECT b.post.id FROM Bookmark b WHERE b.author.id = :authorId)")
  Set<Post> findBookmarkedPostsByAuthorId(@Param("authorId") Integer authorId);
}
