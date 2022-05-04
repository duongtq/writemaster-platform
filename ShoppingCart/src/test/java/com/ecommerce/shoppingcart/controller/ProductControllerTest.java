package com.ecommerce.shoppingcart.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "/add-user.sql", "/add-category.sql", "/add-product.sql", "/add-order.sql", "/add-order-product.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "/remove-order-product.sql", "/remove-order.sql", "/remove-product.sql", "/remove-category.sql", "/remove-user.sql"
})
public class ProductControllerTest {

  @Autowired
  MockMvc mockMvc;

  @WithMockUser(username = "test", password = "1234")
  @Test
  public void findProductById_basic() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/1").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andExpect(jsonPath("$.price").isNotEmpty())
            .andExpect(jsonPath("$.brand").isNotEmpty())
            .andExpect(jsonPath("$.color").isNotEmpty())
            .andExpect(jsonPath("$.category").isNotEmpty())
            .andExpect(jsonPath("$.quantity").isNotEmpty());

  }
}
