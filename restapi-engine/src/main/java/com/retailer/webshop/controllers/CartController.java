package com.retailer.webshop.controllers;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.Order;
import com.retailer.webshop.mappers.ObjectMapper;
import com.retailer.webshop.rest.api.CartApi;
import com.retailer.webshop.rest.model.AddProductToCartDto;
import com.retailer.webshop.rest.model.CartDto;
import com.retailer.webshop.rest.model.CheckoutDto;
import com.retailer.webshop.rest.model.OrderDto;
import com.retailer.webshop.services.CartService;
import com.retailer.webshop.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController implements CartApi {

  private final CartService cartService;

  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<CartDto> addProductToCart(String cartId,
      AddProductToCartDto addProductToCartDto) {
    Cart cart = cartService.addProductToCart(IdUtil.getUUID(cartId),
        addProductToCartDto.getProductId(),
        addProductToCartDto.getQuantity());

    return ResponseEntity.ok(objectMapper.map(cart));
  }

  @Override
  public ResponseEntity<CartDto> createCart() {
    return ResponseEntity.ok(objectMapper.map(cartService.createCart()));
  }

  @Override
  public ResponseEntity<OrderDto> doCheckout(String cartId, CheckoutDto checkoutDto) {
    Order order = cartService.checkout(IdUtil.getUUID(cartId), checkoutDto.getFirstName(),
        checkoutDto.getLastName(), checkoutDto.getAddress(), checkoutDto.getMobile(),
        checkoutDto.getEmail());

    return ResponseEntity.ok(objectMapper.map(order));
  }

  @Override
  public ResponseEntity<CartDto> getCart(String cartId) {
    Cart cart = cartService.getCart(IdUtil.getUUID(cartId));
    return ResponseEntity.ok(objectMapper.map(cart));
  }
}
