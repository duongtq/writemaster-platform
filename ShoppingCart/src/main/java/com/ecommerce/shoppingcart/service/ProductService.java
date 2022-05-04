package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.ProductDTO;
import com.ecommerce.shoppingcart.entity.Product;

import java.util.List;

public interface ProductService {
  List<ProductDTO> filterProduct(String keyword, Double price);

  List<ProductDTO> findProductByPriceWithPaging(Double price, Integer paging);

  ProductDTO findProductByID(Integer id);

  List<ProductDTO> filterProduct(String keyword, Float price);

  ProductDTO decreaseProductQuantity(Product product, Integer number);

  ProductDTO increaseProductQuantity(Product product, Integer number);

  Product findProductEntityByID(Integer id);

}
