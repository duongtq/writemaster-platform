package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @ApiOperation(value = "Register new user")
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> register(@RequestBody UserDTO payload) {
    UserDTO userDTO = userService.createUser(payload);
    return ResponseEntity.ok(userDTO);
  }
}
