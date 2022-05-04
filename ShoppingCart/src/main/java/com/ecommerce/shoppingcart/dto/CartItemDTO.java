package com.ecommerce.shoppingcart.dto;

import com.ecommerce.shoppingcart.entity.Product;
import lombok.Data;

@Data
public class CartItemDTO {
  private Integer id;
  private Integer cartId;
  private Product product;
  private int quantity;
}
