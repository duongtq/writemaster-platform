package com.writemaster.platform.security;

import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Authority;
import com.writemaster.platform.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
  private static final Logger LOG = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);
  private final AuthorRepository authorRepository;

  public JwtUserDetailsServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOG.info("Load author by username {}: START", username);
    Author author = findAuthorByUsername(username);

    // NO LONGER NEED TO ENCODE PASSWORD HERE
    UserBuilder userBuilder;
    if (Optional.ofNullable(author).isPresent()) {
      userBuilder = User.withUsername(username)
              .password(author.getPassword())
              .roles(author.getAuthorities()
                      .stream()
                      .map(Authority::getAuthority)
                      .collect(Collectors.toList())
                      .toArray(String[]::new));
    } else {
      LOG.error("Load author by username {}: FAILED", username);
      throw new UsernameNotFoundException("User not found");
    }
    LOG.info("Load author by username {}: DONE", username);
    return userBuilder.build();
  }

  private Author findAuthorByUsername(String username) {
    return authorRepository.findByUsername(username);
  }
}
