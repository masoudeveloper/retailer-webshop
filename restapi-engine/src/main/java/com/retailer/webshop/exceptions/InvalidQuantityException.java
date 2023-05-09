package com.retailer.webshop.exceptions;

public class InvalidQuantityException extends RuntimeException {

  public InvalidQuantityException() {
    super("The quantity must be an integer greater than zero.");
  }
}
