package com.ecommerce.shoppingcart.mapper;

import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserDTO fromEntity(User entity) {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(entity.getUsername());
    userDTO.setAddress(entity.getAddress());
    userDTO.setEmail(entity.getEmail());
    userDTO.setPassword(entity.getPassword());
    userDTO.setPhoneNumber(entity.getPhoneNumber());
    return userDTO;
  }

  public User fromDTO(UserDTO dto) {
    User entity = new User();
    entity.setUsername(dto.getUsername());
    entity.setAddress(dto.getAddress());
    entity.setEmail(dto.getEmail());
    entity.setPassword(dto.getPassword());
    entity.setPhoneNumber(dto.getPhoneNumber());
    return entity;
  }
}
