package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.ProductDTO;
import com.ecommerce.shoppingcart.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @ApiOperation(value = "Get a product using id", response = ProductDTO.class)
  @GetMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductDTO> getProduct(@PathVariable Integer id) {
    ProductDTO product = productService.findProductByID(id);
    return ResponseEntity.ok(product);
  }

  @GetMapping(value = "/product/filter")
  public ResponseEntity<List<ProductDTO>> filter(@RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "price", required = false) Double price) {
    List<ProductDTO> products = productService.filterProduct(keyword, price);
    return ResponseEntity.ok(products);
  }
  // TODO: pageable + Spring data JPA specification
  // SOLID

  @ApiOperation(value = "Get all product")
  @GetMapping(value = "/product/all")
  public ResponseEntity<List<ProductDTO>> getAll() {
    List<ProductDTO> products = productService.findProductByPriceWithPaging(100.0, 5);
    return ResponseEntity.ok(products);
  }
}
