package com.ecommerce.shoppingcart.mapper;

import com.ecommerce.shoppingcart.dto.CartDTO;
import com.ecommerce.shoppingcart.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
  /**
   * Map an Entity {@link Cart} to DTO {@link CartDTO}
   *
   * @param cart An Entity Cart needs to be mapped
   * @return {@link CartDTO} mapped from Entity input
   */
  public CartDTO fromEntity(Cart cart) {
    CartDTO dto = new CartDTO();
    dto.setId(cart.getId());
    dto.setUsername(cart.getUser().getUsername());
    return dto;
  }
}
