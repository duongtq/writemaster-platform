package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
  Order findOrderById(int orderId);
}
