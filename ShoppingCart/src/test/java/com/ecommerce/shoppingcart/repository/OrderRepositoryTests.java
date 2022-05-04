package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "/add-user.sql", "/add-category.sql", "/add-product.sql", "/add-order.sql", "/add-order-product.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "/remove-order-product.sql", "/remove-order.sql", "/remove-product.sql", "/remove-category.sql", "/remove-user.sql"
})
public class OrderRepositoryTests {
  @Autowired
  OrderRepository orderRepository;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Test
  public void testGetAllOrders() {
    List<Order> orders = orderRepository.findAll();
    assertEquals(2, orders.size());
  }

  @ParameterizedTest(name = "Test: Get Order by valid ID should return correct ID")
  @ValueSource(ints = {1, 2})
  public void testGetOrderByValidIdShouldReturnCorrectOrder(int orderId) {
    Optional<Order> order = orderRepository.findById(orderId);
    assertTrue(order.isPresent());
    assertEquals(orderId, order.get().getId());
  }

  @ParameterizedTest(name = "Test: Get Order by invalid ID should return null")
  @ValueSource(ints = {100, 1000, 10000})
  public void testGetOrderByInvalidIdShouldReturnNull(int orderId) {
    Optional<Order> order = orderRepository.findById(orderId);
    assertTrue(order.isEmpty());
  }

  @ParameterizedTest(name = "Test: Delete Order by valid ID should delete that Order")
  @ValueSource(ints = {1, 2})
  public void testDeleteOrderByValidIdShouldDeleteOrder(int orderId) {
    Order order1 = orderRepository.findOrderById(orderId);
    orderProductRepository.deleteAllByOrder(order1);
    orderRepository.deleteById(orderId);
    Order order2 = orderRepository.findOrderById(orderId);
    assertTrue(Optional.ofNullable(order2).isEmpty());
  }
}
