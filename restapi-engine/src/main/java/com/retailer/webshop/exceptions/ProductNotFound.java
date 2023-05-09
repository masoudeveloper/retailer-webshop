package com.retailer.webshop.exceptions;

public class ProductNotFound extends RuntimeException {

  public ProductNotFound(long productId) {
    super("The product with ID '%s' could not be found.".formatted(productId));
  }
}
