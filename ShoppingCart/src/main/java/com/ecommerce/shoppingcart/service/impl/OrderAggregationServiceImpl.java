package com.ecommerce.shoppingcart.service.impl;

import com.ecommerce.shoppingcart.repository.OrderProductRepository;
import com.ecommerce.shoppingcart.service.OrderAggregationService;
import com.ecommerce.shoppingcart.entity.Order;
import com.ecommerce.shoppingcart.entity.OrderProduct;
import com.ecommerce.shoppingcart.entity.Product;
import com.ecommerce.shoppingcart.payload.OrderPayload;
import com.ecommerce.shoppingcart.payload.ProductPayload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderAggregationServiceImpl implements OrderAggregationService {
  private final OrderProductRepository orderProductRepository;

  public OrderAggregationServiceImpl(OrderProductRepository orderProductRepository) {
    this.orderProductRepository = orderProductRepository;
  }

  public OrderPayload aggregate(Order order) {
    List<OrderProduct> orderProductList = orderProductRepository.findAllByOrder(order);
    OrderPayload orderPayload = new OrderPayload();
    List<ProductPayload> productPayloads = new ArrayList<>();

    for (OrderProduct orderProduct : orderProductList) {
      ProductPayload payload = new ProductPayload();
      Product product = orderProduct.getProduct();

      payload.setId(product.getId());
      payload.setName(product.getName());
      payload.setPrice(product.getPrice());
      payload.setQuantity(orderProduct.getQuantity());

      productPayloads.add(payload);
    }

    orderPayload.setOrderId(order.getId());
    orderPayload.setUsername(order.getUser().getUsername());
    orderPayload.setDateCreated(order.getDateCreated());
    orderPayload.setTotalCost(order.getTotalCost());
    orderPayload.setState(order.getState());
    orderPayload.setProducts(productPayloads);

    return orderPayload;
  }
}
