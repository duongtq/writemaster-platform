package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.CartDTO;
import com.ecommerce.shoppingcart.entity.Cart;
import com.ecommerce.shoppingcart.entity.User;
import javassist.NotFoundException;

public interface CartService {
  Cart cartCheck(User user) throws NotFoundException;

  Cart createCart(User user) throws NotFoundException;


  Cart mapFromDTO(CartDTO payload);
}
