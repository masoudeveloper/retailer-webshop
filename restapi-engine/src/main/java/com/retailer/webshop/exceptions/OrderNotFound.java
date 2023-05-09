package com.retailer.webshop.exceptions;

public class OrderNotFound extends RuntimeException {

  public OrderNotFound(long orderId) {
    super("The order with ID '%d' could not be found.".formatted(orderId));
  }
}
