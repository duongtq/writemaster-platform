package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.entity.User;

public interface UserService {
  User findByUsername(String username);

  UserDTO createUser(UserDTO userDTO);
}
