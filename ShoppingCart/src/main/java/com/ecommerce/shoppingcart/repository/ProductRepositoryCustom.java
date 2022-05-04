package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
  List<Product> filter(String keyword, Double price);
}
