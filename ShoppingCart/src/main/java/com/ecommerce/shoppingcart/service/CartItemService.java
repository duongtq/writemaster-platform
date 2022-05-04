package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.CartItemDTO;
import com.ecommerce.shoppingcart.entity.CartItem;
import com.ecommerce.shoppingcart.entity.Product;
import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface CartItemService {
  CartItemDTO addToCart(CartItemDTO payload, HttpServletRequest request) throws NotFoundException;

  CartItemDTO deleteProductInCart(Integer productId);

  CartItemDTO increaseProductQuantityOfCart(Product product, CartItem cartItem, Integer quantity);

  CartItemDTO decreaseProductQuantityOfCart(Product product, CartItem cartItem, Integer quantity);

  CartItemDTO adjustProductQuantityInCart(Integer productId, Integer quantity);

}
