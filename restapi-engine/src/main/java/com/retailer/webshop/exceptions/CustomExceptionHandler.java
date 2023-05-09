package com.retailer.webshop.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

//  @ExceptionHandler(InvalidProductName.class)
//  public ResponseEntity<String> handleInvalidProductName(InvalidProductName ex) {
//    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//  }
}
