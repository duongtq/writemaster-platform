package com.ecommerce.shoppingcart.service.impl;

import com.ecommerce.shoppingcart.exception.InvalidDataFormatException;
import com.ecommerce.shoppingcart.repository.UserRepository;
import com.ecommerce.shoppingcart.service.UserService;
import com.ecommerce.shoppingcart.common.DataValidator;
import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.entity.User;
import com.ecommerce.shoppingcart.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User findByUsername(String username) {
    // TODO Auto-generated method stub
    return null;
  }

  @Transactional
  @Override
  public UserDTO createUser(UserDTO userDTO) {
    if (!DataValidator.validateUserDTO(userDTO)) {
      throw new InvalidDataFormatException("Invalid data format");
    }
    User user = userMapper.fromDTO(userDTO);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return userDTO;
  }
}
