package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.entity.Cart;
import com.ecommerce.shoppingcart.entity.CartItem;
import com.ecommerce.shoppingcart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
  List<CartItem> findByCart(Cart cart);

  CartItem findByProduct(Product product);
}
