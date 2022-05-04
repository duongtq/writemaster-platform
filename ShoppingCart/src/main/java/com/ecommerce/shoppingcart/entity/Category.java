package com.ecommerce.shoppingcart.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @Column(name = "code", nullable = false)
  private String code;
  @Column(name = "name")
  private String name;
}
