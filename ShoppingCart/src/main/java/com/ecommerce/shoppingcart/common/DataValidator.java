package com.ecommerce.shoppingcart.common;

import com.ecommerce.shoppingcart.dto.UserDTO;

public final class DataValidator {
  public static boolean validateUserDTO(UserDTO userDTO) {
    String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    if (userDTO.getUsername().isBlank() || userDTO.getUsername().isEmpty() || userDTO.getUsername().length() < 4) {
      return false;
    }

    if (userDTO.getPassword().isBlank() || userDTO.getPassword().isEmpty() || userDTO.getPassword().length() < 8) {
      return false;
    }

    if (userDTO.getEmail().isBlank() || userDTO.getEmail().isEmpty() || !userDTO.getEmail().matches(EMAIL_REGEX)) {
      return false;
    }

    if (userDTO.getAddress().isBlank() || userDTO.getAddress().isEmpty() || userDTO.getAddress().length() < 4) {
      return false;
    }

    return !userDTO.getPhoneNumber().isBlank() && !userDTO.getPhoneNumber().isEmpty() && userDTO.getPhoneNumber().length() >= 10;
  }
}
