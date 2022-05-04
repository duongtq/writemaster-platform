package com.ecommerce.shoppingcart.dto;

import lombok.Data;

@Data
public class ProductDTO {
  private int id;
  private String name;
  private Double price;
  private String brand;
  private String color;
  private String category;
  private int quantity;
}
