package com.ecommerce.shoppingcart.mapper;

import com.ecommerce.shoppingcart.dto.OrderProductDTO;
import com.ecommerce.shoppingcart.payload.OrderPayload;
import com.ecommerce.shoppingcart.payload.ProductPayload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderProductMapper {
  public List<OrderProductDTO> fromPayload(OrderPayload payload) {
    List<OrderProductDTO> orderProductList = new ArrayList<>();
    List<ProductPayload> products = payload.getProducts();
    for (ProductPayload product : products) {
      OrderProductDTO orderProductDTO = new OrderProductDTO();
      orderProductDTO.setOrderId(payload.getOrderId());
      orderProductDTO.setProductId(product.getId());
      orderProductDTO.setQuantity(product.getQuantity());
      orderProductList.add(orderProductDTO);
    }
    return orderProductList;
  }
}
