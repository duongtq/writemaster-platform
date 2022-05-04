package com.writemaster.platform.service;

import com.writemaster.platform.dto.PostDTO;
import com.writemaster.platform.payload.CreatePostPayload;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PostService {
  List<PostDTO> getAllPostList();

  PostDTO findPostById(Integer id);

  void deletePostById(Integer id);

  PostDTO createPost(CreatePostPayload createPostPayload);

  PostDTO updatePost(Integer id, PostDTO payload);

  List<PostDTO> findByTitleLike(String keyword);

  List<PostDTO> findPostsByAuthor(String username);

  List<PostDTO> findByTitle(String title);

  List<PostDTO> findTop10NewestPost();

  List<PostDTO> findPostByCreatedDate(LocalDate createdDate);

  List<PostDTO> findPostByAuthorInMonth(Integer authorId, int month);

  Set<PostDTO> findBookmarkedPostsByAuthorId(Integer authorId);
}
