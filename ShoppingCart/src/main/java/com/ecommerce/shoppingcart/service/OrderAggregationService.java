package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.entity.Order;
import com.ecommerce.shoppingcart.payload.OrderPayload;

public interface OrderAggregationService {
  OrderPayload aggregate(Order order);
}
