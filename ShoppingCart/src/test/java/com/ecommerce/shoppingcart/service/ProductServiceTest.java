package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.mapper.ProductMapper;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
  @InjectMocks
  ProductServiceImpl underTest;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private ProductMapper productMapper;

  @DisplayName("Test findProductById() func")
  @Nested
  public class testFindProductByID {
    @Test
    public void findProductByIdReturnTrueWhenGiveExactlyID() {

    }


  }
}
