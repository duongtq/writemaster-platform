package com.ecommerce.shoppingcart.repository.impl;

import com.ecommerce.shoppingcart.repository.ProductRepositoryCustom;
import com.ecommerce.shoppingcart.entity.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
  @PersistenceContext
  EntityManager entityManager;

  @Override
  public List<Product> filter(String keyword, Double price) {
    String sql = "SELECT p FROM Product p";
    List<String> conditions = new ArrayList<>();
    if (keyword != null) {
      conditions.add("(p.category.name LIKE :category OR p.brand LIKE :brand OR p.name LIKE :name OR p.color LIKE :color)");
    }
    if (price != null) {
      conditions.add("(p.price = :price)");
    }
    if (!conditions.isEmpty()) {
      String t = String.join(" AND ", conditions);
      sql = sql + " WHERE " + t;
    }
    Query query = entityManager.createQuery(sql, Product.class);
    if (keyword != null) {
      query
              .setParameter("category", "%" + keyword + "%")
              .setParameter("brand", "%" + keyword + "%")
              .setParameter("name", "%" + keyword + "%")
              .setParameter("color", "%" + keyword + "%");
    }
    if (price != null) {
      query
              .setParameter("price", price);
    }
    return query.getResultList();
  }
}
