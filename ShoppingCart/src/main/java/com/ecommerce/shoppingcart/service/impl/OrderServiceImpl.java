package com.ecommerce.shoppingcart.service.impl;

import com.ecommerce.shoppingcart.exception.InvalidDataFormatException;
import com.ecommerce.shoppingcart.exception.OrderNotFoundException;
import com.ecommerce.shoppingcart.exception.ProductNotFoundException;
import com.ecommerce.shoppingcart.exception.UserNotFoundException;
import com.ecommerce.shoppingcart.repository.OrderProductRepository;
import com.ecommerce.shoppingcart.repository.OrderRepository;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.repository.UserRepository;
import com.ecommerce.shoppingcart.service.OrderAggregationService;
import com.ecommerce.shoppingcart.service.OrderService;
import com.ecommerce.shoppingcart.common.BaseConstants;
import com.ecommerce.shoppingcart.dto.OrderDTO;
import com.ecommerce.shoppingcart.dto.OrderProductDTO;
import com.ecommerce.shoppingcart.entity.Order;
import com.ecommerce.shoppingcart.entity.OrderProduct;
import com.ecommerce.shoppingcart.entity.Product;
import com.ecommerce.shoppingcart.entity.User;
import com.ecommerce.shoppingcart.mapper.OrderMapper;
import com.ecommerce.shoppingcart.mapper.OrderProductMapper;
import com.ecommerce.shoppingcart.payload.OrderPayload;
import com.ecommerce.shoppingcart.payload.ProductPayload;
import com.ecommerce.shoppingcart.util.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
  private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final OrderProductRepository orderProductRepository;
  private final OrderMapper orderMapper;
  private final OrderProductMapper orderProductMapper;
  private final OrderAggregationService orderAggregationService;

  public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderProductRepository orderProductRepository, OrderMapper orderMapper, OrderProductMapper orderProductMapper, OrderAggregationService orderAggregationService) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.orderProductRepository = orderProductRepository;
    this.orderMapper = orderMapper;
    this.orderProductMapper = orderProductMapper;
    this.orderAggregationService = orderAggregationService;
  }

  /**
   * Get all Orders
   *
   * @return The Order list
   */
  @Transactional
  @Override
  public List<OrderPayload> getAllOrders() {
    return orderRepository.findAll().stream().map(orderAggregationService::aggregate).collect(Collectors.toList());
  }

  /**
   * Get an Order by Order ID
   *
   * @param orderId Order ID
   * @return Order with given ID
   */
  @Transactional
  @Override
  public OrderPayload getOrderById(Integer orderId) {
    logger.info("Get Order by ID: START");
    Order order = orderRepository.findOrderById(orderId);
    if (Optional.ofNullable(order).isEmpty()) {
      logger.error("Get Order by ID: ERROR - No order of id {}", orderId);
      throw new OrderNotFoundException("No order of id " + orderId);
    }
    return orderAggregationService.aggregate(order);
  }

  /**
   * Pay an Order by Order ID
   *
   * @param orderId Order ID
   * @return Order with state 'paid'
   */
  @Transactional
  @Override
  public OrderPayload payOrderById(Integer orderId) {
    logger.info("Pay an Order By ID: START");
    Order currentOrder = orderRepository.findOrderById(orderId);
    if (currentOrder == null) {
      logger.error("Pay an Order By ID: ERROR - No order of id {}", orderId);
      throw new OrderNotFoundException("No order of id " + orderId);
    }
    currentOrder.setState(BaseConstants.ORDER_PAID);
    orderRepository.save(currentOrder);
    logger.info("Pay an Order By ID: DONE");
    return orderAggregationService.aggregate(currentOrder);
  }

  /**
   * Create an Order
   *
   * @param payload Payload that contains order info and product info (id, quantity)
   * @return Order with state 'paid'
   */
  @Transactional
  @Override
  public OrderPayload createOrder(OrderPayload payload) {
    // persist order first
    Optional<User> user = userRepository.findByUsername(payload.getUsername());
    if (user.isEmpty()) {
      throw new UserNotFoundException("No user of username " + payload.getUsername());
    }
    OrderDTO orderDTO = orderMapper.fromPayload(payload);
    Order order = orderMapper.fromDTO(orderDTO);
    order.setUser(user.get());
    orderRepository.save(order);

    // then persist the order-product
    List<ProductPayload> actualPayload = new ArrayList<>();

    List<OrderProductDTO> orderProductList = orderProductMapper.fromPayload(payload);
    for (OrderProductDTO orderProductDTO : orderProductList) {
      OrderProduct orderProduct = new OrderProduct();

      Product product = productRepository.findProductById(orderProductDTO.getProductId());
      if (Optional.ofNullable(product).isEmpty()) {
        throw new ProductNotFoundException("No product of id " + orderProductDTO.getProductId());
      }

      int currentQuantity = product.getQuantity();
      int quantity = orderProductDTO.getQuantity();
      if (quantity > currentQuantity || quantity < 0) {
        throw new InvalidDataFormatException("Invalid quantity value: " + quantity);
      }

      ProductPayload payload1 = new ProductPayload();
      payload1.setPrice(product.getPrice());
      payload1.setQuantity(quantity);
      actualPayload.add(payload1);

      // update product's quantity: newQuantity = currentQuantity - quantity
      product.setQuantity(currentQuantity - quantity);

      orderProduct.setOrder(order);
      orderProduct.setProduct(product);
      orderProduct.setQuantity(quantity);

      productRepository.save(product);
      orderProductRepository.save(orderProduct);
    }

    float totalCost = Calculator.getTotalCost(actualPayload);
    // Get the newly created Order
    List<Order> orders = orderRepository.findAll();
    Order newOrder = orders.get(orders.size() - 1);
    newOrder.setTotalCost(totalCost);
    orderDTO.setId(newOrder.getId());
    orderDTO.setTotalCost(totalCost);

    // persist the order with correct totalCost
    orderRepository.save(newOrder);

    // return the OrderDTO only
    return orderAggregationService.aggregate(newOrder);
  }

  /**
   * Delete an Order by Order ID
   *
   * @param orderId Order ID
   */
  @Transactional
  @Override
  public void deleteOrderById(Integer orderId) {
    Order order = orderRepository.findOrderById(orderId);
    if (Optional.ofNullable(order).isEmpty()) {
      throw new OrderNotFoundException("No order of id " + orderId);
    }

    List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
    // when order is deleted, all products in that order will be returned to the system
    for (OrderProduct orderProduct : orderProducts) {
      Product product = productRepository.findProductById(orderProduct.getProduct().getId());
      int currentQuantity = product.getQuantity();
      product.setQuantity(currentQuantity + orderProduct.getQuantity());
      productRepository.save(product);
    }

    orderProductRepository.deleteAllByOrder(order);
    orderRepository.deleteById(orderId);
  }
}
