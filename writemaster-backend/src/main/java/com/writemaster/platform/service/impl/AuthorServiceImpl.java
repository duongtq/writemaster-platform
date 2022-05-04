package com.writemaster.platform.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.DuplicateAuthorException;
import com.writemaster.platform.exception.DuplicateEmailException;
import com.writemaster.platform.exception.InvalidDataFormatException;
import com.writemaster.platform.mapper.AuthorMapper;
import com.writemaster.platform.payload.UpdateAvatarPayload;
import com.writemaster.platform.payload.UpdateBioPayload;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.AuthorityRepository;
import com.writemaster.platform.service.AuthorService;
import com.writemaster.platform.utils.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
  private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
  private final AuthorRepository authorRepository;
  private final AuthorityRepository authorityRepository;
  private final AuthorMapper authorMapper;
  private final PasswordEncoder passwordEncoder;
  private final Cloudinary cloudinary;

  public AuthorServiceImpl(AuthorRepository authorRepository, AuthorityRepository authorityRepository, AuthorMapper authorMapper, PasswordEncoder passwordEncoder, Cloudinary cloudinary) {
    this.authorRepository = authorRepository;
    this.authorityRepository = authorityRepository;
    this.authorMapper = authorMapper;
    this.passwordEncoder = passwordEncoder;
    this.cloudinary = cloudinary;
  }

  @Override
  @Transactional
  public List<AuthorDTO> getAllAuthors() {
    logger.info("Get all authors: START");
    List<Author> authors = authorRepository.findAll();
    logger.info("Get all authors: DONE");
    return authors.stream().map(authorMapper::fromAuthorEntity).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public AuthorDTO findAuthorByUsername(String username) {
    logger.info("Find author by username {}: DONE", username);
    Author author = authorRepository.findByUsername(username);
    if (Optional.ofNullable(author).isEmpty()) {
      logger.error("Find author by username {}: FAILED - No author of username {}", username, username);
      throw new AuthorNotFoundException("No author with username: " + username);
    }
    logger.info("Find author by username {}: DONE", username);
    return authorMapper.fromAuthorEntity(author);
  }

  @Override
  @Transactional
  public boolean checkExistenceByUsername(String username) {
    logger.info("Check existence of username {}: START", username);
    logger.info("Check existence of username {}: DONE", username);
    return authorRepository.existsByUsername(username);
  }

  @Override
  @Transactional
  public AuthorDTO findAuthorById(int id) {
    logger.info("Find author by id {}: START", id);
    Optional<Author> author = authorRepository.findById(id);
    if (author.isEmpty()) {
      logger.error("Find author by id {}: FAILED - No author of id {}", id, id);
      throw new AuthorNotFoundException("No author with id: " + id);
    }
    return authorMapper.fromAuthorEntity(author.get());
  }

  @Override
  @Transactional
  public void createAuthor(AuthorDTO authorDTO) {
    logger.info("Create new author: START");
    Author author = authorRepository.findByUsername(authorDTO.getUsername());
    if (Optional.ofNullable(author).isPresent()) {
      logger.error("Create new author: FAILED - Duplicate author");
      throw new DuplicateAuthorException("Author " + authorDTO.getUsername() + " already registered");
    }
    // CHECK IF EMAIL AlREADY REGISTERED
    Author author1 = authorRepository.findAuthorByEmail(authorDTO.getEmail());
    if (Optional.ofNullable(author1).isPresent()) {
      logger.error("Create new author: FAILED - Duplicate email");
      throw new DuplicateEmailException("Email " + authorDTO.getEmail() + " already registered");
    }

    if (DataValidator.validateAuthorDTO(authorDTO)) {
      logger.error("Create new author: FAILED - Invalid payload data format");
      throw new InvalidDataFormatException("Invalid data format");
    }

    Author newAuthor = authorMapper.fromAuthorDTO(authorDTO);

    // HASH PASSWORD BEFORE PERSISTING TO DATABASE
    newAuthor.setPassword(passwordEncoder.encode(newAuthor.getPassword()));
    authorRepository.save(newAuthor);
    logger.info("Create new author: DONE");
  }

  @Override
  public AuthorDTO updateAvatar(UpdateAvatarPayload updateAvatarPayload) {
    Author author = authorRepository.findAuthorById(updateAvatarPayload.getAuthorId());
    author.setAvatarUrl(updateAvatarPayload.getAvatarUrl());
    return authorMapper.fromAuthorEntity(authorRepository.save(author));
  }

  @Override
  public AuthorDTO updateBio(UpdateBioPayload updateBioPayload) {
    Author author = authorRepository.findAuthorById(updateBioPayload.getAuthorId());
    author.setBio(updateBioPayload.getBio());
    return authorMapper.fromAuthorEntity(authorRepository.save(author));
  }

  @Override
  public AuthorDTO deleteAvatar(UpdateAvatarPayload updateAvatarPayload) throws Exception {
    Author author = authorRepository.findAuthorById(updateAvatarPayload.getAuthorId());
    author.setAvatarUrl(null);
    if (updateAvatarPayload.getAvatarUrl() != null || Objects.equals(updateAvatarPayload.getAvatarUrl(), "")) {
      cloudinary.api().deleteResources(List.of(updateAvatarPayload.getAvatarUrl()), ObjectUtils.emptyMap());
    }
    return authorMapper.fromAuthorEntity(authorRepository.save(author));
  }
}
