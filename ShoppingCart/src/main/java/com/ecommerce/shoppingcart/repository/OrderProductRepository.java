package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.entity.Order;
import com.ecommerce.shoppingcart.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
  void deleteAllByOrder(Order order);

  List<OrderProduct> findAllByOrder(Order order);
}
