package com.writemaster.platform.service;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.exception.NotFoundException;
import com.writemaster.platform.mapper.AuthorMapper;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthorServiceImplTests {
  @Mock
  AuthorRepository authorRepository;

  @Mock
  AuthorMapper authorMapper;

  @InjectMocks
  AuthorServiceImpl underTest;

  @Test
  public void testGetAllAuthorsGet4PostsShouldCallMapper4Times() {
    Author tranDuong = new Author("tranduong", "1234");
    Author jamesGosling = new Author("JamesGosling", "java1995");
    Author dsk = new Author("NguyenDucMinh", "skdarealest");
    Author nah = new Author("TheDeadNah", "1choi100");
    List<Author> authorList = Arrays.asList(tranDuong, jamesGosling, dsk, nah);
    Mockito.when(authorRepository.findAll()).thenReturn(authorList);
    underTest.getAllAuthors();
    verify(authorMapper, times(4)).fromAuthorEntity(any());
  }

  @Test
  public void testFindAuthorByIdGivenIdExistInDatabaseShouldReturnCorrectAuthor() {
    Author mockAuthor = mock(Author.class);
    when(mockAuthor.getId()).thenReturn(1);
    when(mockAuthor.getFirstName()).thenReturn("James");
    when(mockAuthor.getLastName()).thenReturn("Gosling");
    when(mockAuthor.getUsername()).thenReturn("JamesGosling");
    when(mockAuthor.getPassword()).thenReturn("java1995");
    Mockito.when(authorRepository.findById(anyInt())).thenReturn(Optional.of(mockAuthor));
    when(authorMapper.fromAuthorEntity(mockAuthor)).thenCallRealMethod();

    AuthorDTO author = underTest.findAuthorById(1);
    Assertions.assertEquals(mockAuthor.getId(), author.getId());
    Assertions.assertEquals(mockAuthor.getFirstName(), author.getFirstName());
    Assertions.assertEquals(mockAuthor.getLastName(), author.getLastName());
    Assertions.assertEquals(mockAuthor.getUsername(), author.getUsername());
    Assertions.assertEquals(mockAuthor.getPassword(), author.getPassword());
  }

  @Test
  public void testFindAuthorByIdGivenIdNotFoundShouldThrowNotFoundException() {
    NotFoundException exception = assertThrows(NotFoundException.class, () -> underTest.findAuthorById(1));
    assertEquals("No author with id: 1", exception.getMessage());
    verify(authorMapper, never()).fromAuthorEntity(any(Author.class));
  }

  @Test
  public void testFindAuthorByUsernameGivenCorrectUsernameShouldReturnCorrectAuthor() {
    Author mockAuthor = mock(Author.class);
    when(mockAuthor.getId()).thenReturn(1);
    when(mockAuthor.getUsername()).thenReturn("JamesGosling");
    Mockito.when(authorRepository.findByUsername(anyString())).thenReturn(mockAuthor);
    when(authorMapper.fromAuthorEntity(mockAuthor)).thenCallRealMethod();
    AuthorDTO author = underTest.findAuthorByUsername(anyString());
    Assertions.assertEquals(mockAuthor.getId(), author.getId());
    Assertions.assertEquals(mockAuthor.getUsername(), author.getUsername());
  }
}
