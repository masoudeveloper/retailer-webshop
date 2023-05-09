package com.retailer.webshop.exceptions;

import java.util.UUID;

public class CartNotFound extends RuntimeException {

  public CartNotFound(UUID cardId) {
    super("The cart with ID '%s' could not be found.".formatted(cardId.toString()));
  }
}
