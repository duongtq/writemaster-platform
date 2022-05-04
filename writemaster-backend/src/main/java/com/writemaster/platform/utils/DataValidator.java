package com.writemaster.platform.utils;

import com.writemaster.platform.dto.AuthorDTO;
import com.writemaster.platform.dto.PostDTO;
import com.writemaster.platform.security.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class DataValidator {
  public static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
  private static final Logger logger = LoggerFactory.getLogger(DataValidator.class);

  public static boolean validatePostDTO(PostDTO postDTO) {
    if (Optional.ofNullable(postDTO.getTitle()).isEmpty() || "".equals(postDTO.getTitle())) {
      return true;
    }

    if (Optional.ofNullable(postDTO.getDescription()).isEmpty() || "".equals(postDTO.getDescription())) {
      return true;
    }

    return Optional.ofNullable(postDTO.getContent()).isEmpty() || "".equals(postDTO.getContent());
  }

  public static boolean validateAuthorDTO(AuthorDTO authorDTO) {
    if (Optional.ofNullable(authorDTO.getUsername()).isEmpty() || "".equals(authorDTO.getUsername())) {
      logger.warn("username invalid");
      return true;
    }

    if (Optional.ofNullable(authorDTO.getPassword()).isEmpty() || "".equals(authorDTO.getPassword()) || authorDTO.getPassword().length() < 8) {
      logger.warn("password invalid");
      logger.warn("password {}", authorDTO.getPassword());
      return true;
    }

    if (Optional.ofNullable(authorDTO.getFirstName()).isEmpty() || "".equals(authorDTO.getFirstName())) {
      logger.warn("firstName invalid");

      return true;
    }

    if (Optional.ofNullable(authorDTO.getLastName()).isEmpty() || "".equals(authorDTO.getLastName())) {
      logger.warn("lastName invalid");
      return true;
    }

    if (Optional.ofNullable(authorDTO.getEmail()).isEmpty() || "".equals(authorDTO.getEmail()) || !authorDTO.getEmail().matches(EMAIL_REGEX)) {
      logger.warn("email invalid");
      return true;
    }

    if (Optional.ofNullable(authorDTO.getBirthDate()).isEmpty()) {
      logger.warn("birthdate invalid");
      return true;
    }

    if (Optional.ofNullable(authorDTO.getCreatedDate()).isEmpty()) {
      logger.warn("createdDate invalid");
      return true;
    }

    if (Optional.ofNullable(authorDTO.getAuthorities()).isEmpty() || authorDTO.getAuthorities().isEmpty()) {
      logger.warn("authority null");
      return true;
    }

    List<String> validAuthority = Arrays.asList(Roles.AUTHOR, Roles.ADMIN);
    for (String authority : authorDTO.getAuthorities()) {
      if (!validAuthority.contains(authority)) {
        logger.warn("authority invalid");
        return true;
      }
    }
    logger.warn("all data valid");

    return false;
  }
}
