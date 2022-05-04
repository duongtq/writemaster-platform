package com.ecommerce.shoppingcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecommerce.shoppingcart.exception.NotFoundException;
import com.ecommerce.shoppingcart.exception.ProductNotFoundException;
import com.ecommerce.shoppingcart.exception.UserNotFoundException;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import java.util.Objects;

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
public class OrderControllerIntTests {
  @Autowired
  MockMvc mockMvc;

  public String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

    @DisplayName(value = "Test Get Order should work properly")
    @Nested
    @WithMockUser(username = "test", password = "1234")
    public class TestGetOrder {
      @Test
      public void testGetAllOrdersShouldReturnAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/order/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(2)))
                .andExpect(jsonPath("$[*].orderId").isNotEmpty());
      }

      @ParameterizedTest(name = "Test Get Order by correct ID should return correct Order")
      @ValueSource(ints = {1, 2})
      public void testGetOrderByIdShouldReturnCorrectOrder(int orderId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/order/" + orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.dateCreated").isNotEmpty())
                .andExpect(jsonPath("$.totalCost").isNotEmpty())
                .andExpect(jsonPath("$.state").isNotEmpty());
      }

      @ParameterizedTest(name = "Test Get Order by invalid ID should throw NotFoundException")
      @ValueSource(ints = {10, 100, 1000})
      public void testGetOrderByIdGivenNonExistentIdShouldThrowNotFoundException(int orderId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/order/" + orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof NotFoundException))
                .andExpect(result -> Assertions.assertEquals(
                        Objects.requireNonNull(result.getResolvedException()).getMessage(), "No order of id " + orderId));
      }
    }

    @DisplayName(value = "Test: Create Order should work properly")
    @Nested
    @WithMockUser(username = "test", password = "1234")
    public class TestCreateOrder {

      @Test
      public void testCreateOrderGivenValidPayloadShouldCreateOrder() throws Exception {
        String validPayload =
                "{\n" +
                "  \"dateCreated\": \"2021-11-18T14:36:06.030Z\",\n" +
                "  \"orderId\": 0,\n" +
                "  \"products\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"lorem ipsum\",\n" +
                "      \"price\": 0,\n" +
                "      \"quantity\": 2\n" +
                "    }\n" +
                "  ],\n" +
                "  \"state\": \"UNPAID\",\n" +
                "  \"totalCost\": 0,\n" +
                "  \"username\": \"test\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/order/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.dateCreated").isNotEmpty())
                .andExpect(jsonPath("$.state").value("UNPAID"))
                .andExpect(jsonPath("$.totalCost").isNotEmpty())
                .andExpect(jsonPath("$.products", IsCollectionWithSize.hasSize(1)));
      }

      @Test
      public void testCreateOrderGivenInvalidPayloadNoUsernameShouldThrowException() throws Exception {
        String validPayload =
                "{\n" +
                "  \"dateCreated\": \"2021-11-18T14:36:06.030Z\",\n" +
                "  \"orderId\": 0,\n" +
                "  \"products\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"lorem ipsum\",\n" +
                "      \"price\": 0,\n" +
                "      \"quantity\": 2\n" +
                "    }\n" +
                "  ],\n" +
                "  \"state\": \"UNPAID\",\n" +
                "  \"totalCost\": 0,\n" +
                "  \"username\": \"null-user\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof UserNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No user of username null-user",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
      }

      @Test
      public void testCreateOrderGivenNonExistentProductShouldThrowException() throws Exception {
        String validPayload = "{\n" +
                "  \"dateCreated\": \"2021-11-18T14:36:06.030Z\",\n" +
                "  \"orderId\": 0,\n" +
                "  \"products\": [\n" +
                "    {\n" +
                "      \"id\": 1000,\n" +
                "      \"name\": \"lorem ipsum\",\n" +
                "      \"price\": 0,\n" +
                "      \"quantity\": 2\n" +
                "    }\n" +
                "  ],\n" +
                "  \"state\": \"UNPAID\",\n" +
                "  \"totalCost\": 0,\n" +
                "  \"username\": \"test\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof ProductNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No product of id 1000",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
      }
    }

    @DisplayName(value = "Test: Delete Order should work properly")
    @Nested
    @WithMockUser(username = "test", password = "1234")
    public class TestDeleteOrder {
      @ParameterizedTest(name = "Test Delete Order by correct ID should delete Order")
      @ValueSource(ints = {1, 2})
      public void testDeleteOrderByIdGivenValidIdShouldDeleteOrder(int orderId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/order/" + orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
      }

      @ParameterizedTest(name = "Test Delete Order by invalid ID should throw exception")
      @ValueSource(ints = {10, 100, 1000})
      public void testDeleteOrderByIdGivenInvalidIdShouldThrowException(int orderId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/order/" + orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> Assertions.assertEquals("No order of id " + orderId, Objects.requireNonNull(result.getResolvedException()).getMessage()));
      }
    }
}
