package com.ecommerce.shoppingcart.util;

import com.ecommerce.shoppingcart.config.JwtTokenUtil;
import com.ecommerce.shoppingcart.entity.User;
import com.ecommerce.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  public User getUser(HttpServletRequest request) {
    return userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization").substring(7))).get();
  }
}
