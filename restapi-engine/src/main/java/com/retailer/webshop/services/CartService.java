package com.retailer.webshop.services;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.Order;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface CartService {

  Cart getCart(UUID id);

  Cart createCart();

  Cart addProductToCart(UUID cartId, Long productId, int quantity);

  Order checkout(UUID cartId, String firstName, String lastName, String address,
      @NotNull String mobile, @NotNull String email);
}
