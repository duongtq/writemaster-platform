package com.ecommerce.shoppingcart.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "products")
public class Product {
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "name")
  private String name;
  @Column(name = "price")
  private Double price;
  @Column(name = "brand")
  private String brand;
  @Column(name = "color")
  private String color;
  @ManyToOne
  @JoinColumn(name = "category_code")
  private Category category;
  @Column(name = "quantity")
  private Integer quantity;
}
