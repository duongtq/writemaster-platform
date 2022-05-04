package com.ecommerce.shoppingcart.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
  private int id;
  private String username;
  private Date dateCreated;
  private float totalCost;
  private String state;
}
