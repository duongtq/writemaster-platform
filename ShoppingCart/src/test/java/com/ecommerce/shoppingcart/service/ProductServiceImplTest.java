package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.ProductDTO;
import com.ecommerce.shoppingcart.entity.Category;
import com.ecommerce.shoppingcart.entity.Product;
import com.ecommerce.shoppingcart.mapper.ProductMapper;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl underTest;

    @DisplayName("Test findProductById() func")
    @Nested
    public class testFindProductByID {
        @Test
        public void findProductByIdReturnTrueWhenGiveExactlyID() {
            Category category = Mockito.mock(Category.class);
            //            create data
            Product product = Mockito.mock(Product.class);
            when(product.getId()).thenReturn(1);
            when(product.getName()).thenReturn("Thang Long");
            when(product.getBrand()).thenReturn("Thang Long");
            when(product.getColor()).thenReturn("Thang Long");
            when(product.getQuantity()).thenReturn(1);
            when(product.getCategory()).thenReturn(category);
            when(product.getPrice()).thenReturn(100.0);
//             define behavior of Repository
            when(productRepository.findProductById(Mockito.anyInt())).thenReturn(product);
//             define behavior of Mapper
            when(productMapper.fromEntity(product)).thenCallRealMethod();
//            call service method
            ProductDTO productDTO = underTest.findProductByID(1);
//            assert the results
            assertEquals(1, productDTO.getId());
//            verify
            verify(productRepository).findProductById(1);
        }
    }
}
