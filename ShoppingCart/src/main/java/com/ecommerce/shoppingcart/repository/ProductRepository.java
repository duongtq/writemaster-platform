package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
  Product findProductById(Integer id);

  Page<Product> findProductsByPrice(Double price, Pageable pageable);
}
