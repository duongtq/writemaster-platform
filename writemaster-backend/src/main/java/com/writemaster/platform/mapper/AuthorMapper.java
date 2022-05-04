package com.writemaster.platform.mapper;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Authority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthorMapper {
  private final LikeMapper likeMapper;
  private final CommentMapper commentMapper;
  private final BookmarkMapper bookmarkMapper;

  public AuthorMapper(LikeMapper likeMapper, CommentMapper commentMapper, BookmarkMapper bookmarkMapper) {
    this.likeMapper = likeMapper;
    this.commentMapper = commentMapper;
    this.bookmarkMapper = bookmarkMapper;
  }

  public AuthorDTO fromAuthorEntity(Author author) {
    AuthorDTO authorDTO = new AuthorDTO();
    authorDTO.setId(author.getId());
    authorDTO.setUsername(author.getUsername());
    authorDTO.setPassword(author.getPassword());
    authorDTO.setFirstName(author.getFirstName());
    authorDTO.setLastName(author.getLastName());
    authorDTO.setEmail(author.getEmail());
    authorDTO.setBirthDate(author.getBirthDate());
    authorDTO.setCreatedDate(author.getCreatedDate());
    authorDTO.setAuthorities(author.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toSet()));
    authorDTO.setRealName(author.getRealName());
    authorDTO.setAvatarUrl(author.getAvatarUrl());
    authorDTO.setBio(author.getBio());
    return authorDTO;
  }

  public Author fromAuthorDTO(AuthorDTO authorDTO) {
    Author author = new Author();
    author.setUsername(authorDTO.getUsername());
    author.setPassword(authorDTO.getPassword());
    author.setFirstName(authorDTO.getFirstName());
    author.setLastName(authorDTO.getLastName());
    author.setEmail(authorDTO.getEmail());
    author.setBirthDate(authorDTO.getBirthDate());
    author.setCreatedDate(authorDTO.getCreatedDate());
    author.setAuthorities(authorDTO.getAuthorities().stream().map(authority -> new Authority(author, authority)).collect(Collectors.toSet()));
    return author;
  }
}
