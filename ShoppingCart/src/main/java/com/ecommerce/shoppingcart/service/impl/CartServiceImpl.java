package com.ecommerce.shoppingcart.service.impl;

import com.ecommerce.shoppingcart.repository.CartRepository;
import com.ecommerce.shoppingcart.repository.UserRepository;
import com.ecommerce.shoppingcart.service.CartService;
import com.ecommerce.shoppingcart.dto.CartDTO;
import com.ecommerce.shoppingcart.entity.Cart;
import com.ecommerce.shoppingcart.entity.User;
import com.ecommerce.shoppingcart.mapper.CartMapper;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

  private final UserRepository userRepository;
  private final CartRepository cartRepository;

  public CartServiceImpl(UserRepository userRepository, CartRepository cartRepository) {
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
  }

  /**
   * Check the existence of a shopping cart belonging to the current user. If not exist, create a new one.
   *
   * @param  user User object input
   * @return An existent Entity Cart or Cart just created
   * @throws NotFoundException        if user not found with User input
   * @throws IllegalArgumentException if User input is null
   */
  @Override
  public Cart cartCheck(User user) throws NotFoundException {
    if (user == null) {
      throw new IllegalArgumentException("Username cannot be null!");
    }
    Optional<Cart> optionalCart = cartRepository.findByUser(user);
    if (optionalCart.isEmpty()) {
      return createCart(user);
    }
    return optionalCart.get();
  }

  /**
   * Create Cart for current User login
   *
   * @param  user object input
   * @return Created Entity Cart
   * @throws NotFoundException        If User not found
   * @throws IllegalArgumentException If User input is null
   */
  @Override
  @Transactional
  public Cart createCart(User user) throws NotFoundException {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null!");
    }
    Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
    if (optionalUser.isEmpty()) {
      throw new NotFoundException("User not found with username: " + user);
    }
    Cart cart = new Cart();
    cart.setUser(optionalUser.get());
    return cartRepository.save(cart);
  }

  /**
   * Mapping CartDTO to Cart
   *
   * @param  payload CartDTO needs to mapped
   * @return An Entity Cart
   */
  @Override
  public Cart mapFromDTO(CartDTO payload) {
    Cart cart = new Cart();
    Optional<User> optionalUser = userRepository.findByUsername(payload.getUsername());
    if (optionalUser.isPresent()) {
      cart.setId(payload.getId());
      cart.setUser(optionalUser.get());
    }
    return cart;
  }

}
