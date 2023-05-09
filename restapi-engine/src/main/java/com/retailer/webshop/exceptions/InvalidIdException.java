package com.retailer.webshop.exceptions;

public class InvalidIdException extends RuntimeException {

  public InvalidIdException() {
    super("The provided ID is not valid.");
  }
}
