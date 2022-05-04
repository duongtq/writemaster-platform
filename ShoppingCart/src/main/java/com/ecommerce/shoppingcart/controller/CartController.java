package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.CartDTO;
import com.ecommerce.shoppingcart.dto.CartItemDTO;
import com.ecommerce.shoppingcart.service.CartItemService;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
  private final CartItemService cartItemService;

  @Autowired
  public CartController(CartItemService cartService) {
    this.cartItemService = cartService;
  }

  @ApiOperation(value = "Add product to cart", response = CartDTO.class)
  @PostMapping(value = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CartItemDTO> addToCart(@RequestBody CartItemDTO payload, HttpServletRequest request) throws NotFoundException {
    CartItemDTO createdCart = cartItemService.addToCart(payload, request);
    return ResponseEntity.ok(createdCart);
  }

  @ApiOperation(value = "Delete product in cart", response = CartDTO.class)
  @DeleteMapping(value = "/cart/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CartItemDTO> deleteProductInCart(@PathVariable Integer cartId, @RequestParam Integer productId) {
    CartItemDTO cartDeleted = cartItemService.deleteProductInCart(productId);
    return ResponseEntity.ok(cartDeleted);
  }

  @ApiOperation(value = "Adjust product's quantity in cart", response = CartDTO.class)
  @PutMapping(value = "/cart/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CartItemDTO> adjustProductQuantityInCart(@RequestParam Integer productId, @RequestParam Integer quantity) {
    CartItemDTO cartUpdated = cartItemService.adjustProductQuantityInCart(productId, quantity);
    return ResponseEntity.ok(cartUpdated);
  }
}
