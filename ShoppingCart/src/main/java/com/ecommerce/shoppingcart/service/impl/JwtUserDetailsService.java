package com.ecommerce.shoppingcart.service.impl;

import com.ecommerce.shoppingcart.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public JwtUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<com.ecommerce.shoppingcart.entity.User> optionalUser = userRepository.findByUsername(username);
    UserBuilder userBuilder;
    if (optionalUser.isPresent()) {
      userBuilder = User.withUsername(username)
              .password(optionalUser.get().getPassword())
              .roles("ANY_ROLE");
      // userBuilder.password(new BCryptPasswordEncoder().encode(optionalUser.get().getPassword()));
    } else {
      throw new UsernameNotFoundException("Username not found");
    }
    return userBuilder.build();
  }

}
