package com.retailer.webshop.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.rest.model.CartDto;
import com.retailer.webshop.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CartService cartService;

  @Test
  void getCart_validCartIdProvided_returnCart() throws Exception {
    Cart cart = cartService.createCart();

    MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/client-api/v1/carts/{cardId}", cart.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String responseJson = result.getResponse().getContentAsString();
    CartDto responseCartDto = new ObjectMapper().readValue(responseJson, CartDto.class);

    assertThat(responseCartDto.getId()).isEqualTo(cart.getId().toString());
  }

  @Test
  void createCart_returnsNewCart() throws Exception {
    MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/client-api/v1/carts"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String responseJson = result.getResponse().getContentAsString();
    CartDto responseCartDto = new ObjectMapper().readValue(responseJson, CartDto.class);

    Cart createdCart = cartService.getCart(UUID.fromString(responseCartDto.getId()));

    assertThat(createdCart).isNotNull();
    assertThat(responseCartDto.getId()).isEqualTo(createdCart.getId().toString());
  }

}